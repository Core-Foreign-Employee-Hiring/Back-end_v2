package com.forwork.backend.api.pay.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forwork.backend.api.pay.dto.external.request.TossPaymentCancelRequestDTO;
import com.forwork.backend.api.pay.dto.external.request.TossPaymentConfirmRequestDTO;
import com.forwork.backend.api.pay.dto.external.response.TossPaymentResponseDTO;
import com.forwork.backend.api.pay.dto.internal.PaymentCancelDTO;
import com.forwork.backend.api.pay.dto.internal.PaymentDTO;
import com.forwork.backend.api.pay.enums.TossPaymentCancelExceptionType;
import com.forwork.backend.api.pay.enums.TossPaymentConfirmExceptionType;
import com.forwork.backend.api.pay.enums.TossPaymentGetExceptionType;
import com.forwork.backend.api.pay.exception.PaymentException;
import com.forwork.backend.api.pay.exception.PaymentTimeoutException;
import com.forwork.backend.api.pay.exception.PaymentUnknownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 연결 타임아웃: 재시도 가능
 * 읽기 타임아웃: 읽기 작업, 멱등성이 보장된 갱신만 할 것.
 * <p>
 * 재시도 횟수: 계속 x 응답 시간도 길어짐. 1 ~ 2회 정도 그 이상이면 외부 시스템 문제 있다고 판단.
 * 재시도 간격: 즉시 할 경우 똑같은 문제로 타임아웃 -> 조금 기달렸다가 시도.
 * <p>
 * 재시도 요청이 많을 경우 외부 서비스 부하 -> 우리도 같이 느려짐. -> 동시 요청 제한 고려(벌크헤드 패턴 등).
 */

@Slf4j
@Component
public class TossPaymentClient extends TossClient {
    private static final String TOSS_PAYMENT_CONFIRM_URL = "/v1/payments/confirm";
    private static final String TOSS_PAYMENT_GET_URL = "/v1/payments/{paymentKey}";
    private static final String TOSS_PAYMENT_CANCEL_URL = "/v1/payments/{paymentKey}/cancel";

    public TossPaymentClient(
            @Qualifier("tossRestTemplate") RestTemplate tossRestTemplate,
            ObjectMapper objectMapper
    ) {
        super(tossRestTemplate, objectMapper);
    }

    /**
     * 결제 승인 요청
     */

    @Retryable(
            retryFor = {ResourceAccessException.class, HttpServerErrorException.class},
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2.0,
                    random = true,
                    maxDelay = 5000
            )
    )
    public PaymentDTO confirmPayment(String paymentKey, String orderId, Long amount) {
        log.info("[confirmPayment][call]");

        HttpHeaders headers = createAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

//        headers.set("TossPayments-Test-Code", "UNKNOWN_PAYMENT_ERROR");

        TossPaymentConfirmRequestDTO request = new TossPaymentConfirmRequestDTO(paymentKey, orderId, amount);

        HttpEntity<TossPaymentConfirmRequestDTO> entity = new HttpEntity<>(request, headers);


        try {

            ResponseEntity<TossPaymentResponseDTO> response = tossRestTemplate.exchange(
                    baseUrl + TOSS_PAYMENT_CONFIRM_URL,
                    HttpMethod.POST,
                    entity,
                    TossPaymentResponseDTO.class
            );

            TossPaymentResponseDTO body = response.getBody();
            return PaymentDTO.from(body);

        } catch (HttpClientErrorException e) { // 4xx 에러
            log.warn("[confirmPayment][4XX 에러 ][paymentKey={}]", paymentKey, e);
            String responseBody = e.getResponseBodyAsString();
            HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());

            String code;
            String message;
            try {
                JsonNode json = objectMapper.readTree(responseBody);
                code = json.path("code").asText();
                message = json.path("message").asText();
            } catch (Exception parseEx) {
                // 파싱 실패 시에도 알 수 없는 예외 던짐
                throw new PaymentUnknownException(status, "Failed to parse error response");
            }

            PaymentException paymentException = TossPaymentConfirmExceptionType.fromErrorCode(
                    status,
                    code,
                    message
            );

            log.info("[confirmPayment][paymentException= {}]", paymentException.getClass());

            throw paymentException;
        }
    }

    @Recover
    public PaymentDTO recoverConfirmPayment(ResourceAccessException e, String paymentKey, String orderId, Long amount) {
        log.error("[confirmPayment][네트워크 에러로 결제 승인 실패][merchantOrderId= {}, paymentKey= {}]", orderId, paymentKey, e);
        throw new PaymentTimeoutException(HttpStatus.REQUEST_TIMEOUT, e.getMessage());
    }

    @Recover
    public PaymentDTO recoverConfirmPayment(HttpServerErrorException e, String paymentKey, String orderId, Long amount) {
        log.error("[confirmPayment][서버 오류로 결제 승인 실패][merchantOrderId= {}, paymentKey= {}]", orderId, paymentKey, e);
        String responseBody = e.getResponseBodyAsString();
        HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());
        String message;

        try {
            JsonNode json = objectMapper.readTree(responseBody);
            message = json.path("message").asText();
        } catch (Exception parseEx) {
            // 파싱 실패 시에도 알 수 없는 예외 던짐
            throw new PaymentUnknownException(status, "Failed to parse error response");
        }
        throw new PaymentTimeoutException(HttpStatus.resolve(e.getStatusCode().value()), message);
    }

    @Recover
    public PaymentDTO recoverConfirmPayment(PaymentException e, String paymentKey, String orderId, Long amount) {
        throw e;
    }


    /**
     * 결제 조회 요청
     * <p>
     * IN_PROGRESS: 승인 요청 x인 겨
     */
    @Retryable(
            retryFor = {ResourceAccessException.class, HttpServerErrorException.class},
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2.0,
                    random = true,
                    maxDelay = 5000
            )
    )
    public PaymentDTO getPaymentByPaymentKey(String paymentKey) {
        log.info("[getPayment][call]");

        HttpHeaders headers = createAuthHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TossPaymentResponseDTO> response = tossRestTemplate.exchange(
                    baseUrl + TOSS_PAYMENT_GET_URL,
                    HttpMethod.GET,
                    entity,
                    TossPaymentResponseDTO.class,
                    paymentKey
            );

            TossPaymentResponseDTO body = response.getBody();
            return PaymentDTO.from(body);

        } catch (HttpClientErrorException e) {
            log.warn("[getPayment][4XX 에러][paymentKey= {}]", paymentKey, e);

            String responseBody = e.getResponseBodyAsString();
            HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());
            String code;
            String message;
            try {
                JsonNode json = objectMapper.readTree(responseBody);
                code = json.path("code").asText();
                message = json.path("message").asText();


            } catch (Exception parseEx) {
                // 파싱 실패 시에도 알 수 없는 예외 던짐
                throw new PaymentUnknownException(status, "Failed to parse error response");
            }

            throw TossPaymentGetExceptionType.fromErrorCode(
                    status,
                    code,
                    message
            );
        }
    }

    @Recover
    public PaymentDTO recoverGetPaymentByPaymentKey(ResourceAccessException e, String paymentKey) {
        log.error("[getPayment][네트워크 에러로 결제 승인 실패][paymentKey= {}]", paymentKey, e);
        throw new PaymentTimeoutException(HttpStatus.REQUEST_TIMEOUT, e.getMessage());
    }

    @Recover
    public PaymentDTO recoverGetPaymentByPaymentKey(HttpServerErrorException e, String paymentKey) {
        log.error("[getPayment][서버 오류로 결제 승인 실패][paymentKey= {}]", paymentKey, e);

        String responseBody = e.getResponseBodyAsString();
        HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());
        String message;

        try {
            JsonNode json = objectMapper.readTree(responseBody);
            message = json.path("message").asText();
        } catch (Exception parseEx) {
            // 파싱 실패 시에도 알 수 없는 예외 던짐
            throw new PaymentUnknownException(status, "Failed to parse error response");
        }
        throw new PaymentTimeoutException(HttpStatus.resolve(e.getStatusCode().value()), message);
    }

    @Recover
    public PaymentDTO recoverGetPaymentByPaymentKey(PaymentException e, String paymentKey) {
        throw e;
    }


    /**
     * 결제 취소
     * TossPaymentStatus: CANCELED, ALREADY_CANCELED, RETRY
     */
    @Retryable(
            retryFor = {ResourceAccessException.class, HttpServerErrorException.class},
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2.0,
                    random = true,
                    maxDelay = 5000
            )
    )
    public List<PaymentCancelDTO> cancelPayment(String paymentKey, String cancelReason) {
        HttpHeaders headers = createAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        TossPaymentCancelRequestDTO request = new TossPaymentCancelRequestDTO(cancelReason, null);
        HttpEntity<TossPaymentCancelRequestDTO> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<TossPaymentResponseDTO> response = tossRestTemplate.exchange(
                    baseUrl + TOSS_PAYMENT_CANCEL_URL,
                    HttpMethod.POST,
                    entity,
                    TossPaymentResponseDTO.class,
                    paymentKey
            );

            TossPaymentResponseDTO body = response.getBody();
            List<TossPaymentResponseDTO.Cancel> cancels = body.cancels();

            List<PaymentCancelDTO> paymentCancelDTOs = PaymentCancelDTO.fromList(cancels);

            return paymentCancelDTOs;

        } catch (HttpClientErrorException e) {
            log.warn("[cancelPayment][4XX 에러 ][paymentKey={}]", paymentKey, e);
            String responseBody = e.getResponseBodyAsString();
            HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());
            String code;
            String message;

            try {
                JsonNode json = objectMapper.readTree(responseBody);
                code = json.path("code").asText();
                message = json.path("message").asText();

            } catch (Exception parseEx) {
                // 파싱 실패 시에도 알 수 없는 예외 던짐
                throw new PaymentUnknownException(status, "Failed to parse error response");
            }

            throw TossPaymentCancelExceptionType.fromErrorCode(
                    status,
                    code,
                    message
            );
        }
    }

    @Recover
    public List<PaymentCancelDTO> recoverCancelPayment(ResourceAccessException e, String paymentKey, String cancelReason) {
        log.error("[recoverCancelPayment][네트워크 에러로 결제 승인 실패][paymentKey= {}]", paymentKey, e);
        throw new PaymentTimeoutException(HttpStatus.REQUEST_TIMEOUT, e.getMessage());
    }

    @Recover
    public List<PaymentCancelDTO> recoverCancelPayment(HttpServerErrorException e, String paymentKey, String cancelReason) {
        log.error("[recoverCancelPayment][서버 오류로 결제 승인 실패][paymentKey= {}]", paymentKey, e);

        String responseBody = e.getResponseBodyAsString();
        HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());
        String message;

        try {
            JsonNode json = objectMapper.readTree(responseBody);
            message = json.path("message").asText();
        } catch (Exception parseEx) {
            // 파싱 실패 시에도 알 수 없는 예외 던짐
            throw new PaymentUnknownException(status, "Failed to parse error response");
        }
        throw new PaymentTimeoutException(HttpStatus.resolve(e.getStatusCode().value()), message);
    }

    @Recover
    public List<PaymentCancelDTO> recoverCancelPayment(PaymentException e, String paymentKey, String cancelReason) {
        throw e;
    }

}

