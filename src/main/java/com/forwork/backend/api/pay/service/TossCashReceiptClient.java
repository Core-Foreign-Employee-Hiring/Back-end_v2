package com.forwork.backend.api.pay.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forwork.backend.api.pay.dto.external.request.CashReceiptRequest;
import com.forwork.backend.api.pay.dto.external.response.TossCashReceiptResponse;
import com.forwork.backend.common.exception.BaseException;
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

@Slf4j
@Component
public class TossCashReceiptClient extends TossClient {
    public static final String CASH_RECEIPT_BASE_URL = "/v1/cash-receipts";
    public static final String CASH_RECEIPT_CANCEL_URL = "/v1/cash-receipts/{receiptKey}/cancel";


    public TossCashReceiptClient(
            @Qualifier("tossRestTemplate") RestTemplate tossRestTemplate,
            ObjectMapper objectMapper
    ) {
        super(tossRestTemplate, objectMapper);
    }


    /**
     * 현금영수증 발금
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
    public TossCashReceiptResponse issueCashReceipt(CashReceiptRequest request) {

        HttpHeaders headers = createAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CashReceiptRequest> entity = new HttpEntity<>(request, headers);


        try {
            ResponseEntity<TossCashReceiptResponse> response = tossRestTemplate.exchange(
                    baseUrl + CASH_RECEIPT_BASE_URL,
                    HttpMethod.POST,
                    entity,
                    TossCashReceiptResponse.class
            );

            TossCashReceiptResponse body = response.getBody();

            return body;


        } catch (HttpClientErrorException e) {
            log.warn("[issueCashReceipt][4XX 에러][merchantOrderId={}]", request.orderId(), e);

            String responseBody = e.getResponseBodyAsString();
            HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());

            String message;
            try {
                JsonNode json = objectMapper.readTree(responseBody);
                message = json.path("message").asText();
            } catch (Exception parseEx) {
                message = "Failed to parse error response";
            }

            throw new BaseException(status, message);
        }
    }

    @Recover
    public TossCashReceiptResponse issueCashReceipt(ResourceAccessException e, CashReceiptRequest request) {
        log.warn("[issueCashReceipt][네트워크 에러][merchantOrderId= {}]", request.orderId(), e);
        throw new BaseException(HttpStatus.REQUEST_TIMEOUT, e.getMessage());
    }

    @Recover
    public TossCashReceiptResponse issueCashReceipt(HttpServerErrorException e, CashReceiptRequest request) {
        log.warn("[issueCashReceipt][원격 서버 오류][merchantOrderId= {}]", request.orderId(), e);
        String responseBody = e.getResponseBodyAsString();
        HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());
        String message;

        try {
            JsonNode json = objectMapper.readTree(responseBody);
            message = json.path("message").asText();
        } catch (Exception parseEx) {
            // 파싱 실패 시에도 알 수 없는 예외 던짐
            throw new BaseException(status, "Failed to parse error response");
        }
        throw new BaseException(HttpStatus.resolve(e.getStatusCode().value()), message);
    }

    @Recover
    public TossCashReceiptResponse issueCashReceipt(BaseException e, CashReceiptRequest request) {
        throw e;
    }
}
