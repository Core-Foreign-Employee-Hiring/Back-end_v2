package com.forwork.backend.api.order.entity;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class OrderPassArchive {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="order_pass_archive_id")
    private Long id;

    private String amount; // 개별 상품에 대한 가격

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="pass_archive_id")
    private PassArchive passArchive;

}
