package com.forwork.backend.api.order.entity;

import com.forwork.backend.api.pass_archive.entity.PassArchive;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
public class OrderPassArchive {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="order_pass_archive_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="pass_archive_id")
    private PassArchive passArchive;

    public OrderPassArchive(Order order, PassArchive passArchive) {
        this.order = order;
        this.passArchive = passArchive;
    }
}
