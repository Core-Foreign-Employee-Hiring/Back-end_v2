package com.forwork.backend.api.notification.entity;

import com.forwork.backend.api.member.entity.Member;
import com.forwork.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Getter
@Entity
@NoArgsConstructor
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name="noti_type")
public abstract class Notification extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="notification_id")
    private Long id;

    private boolean isRead;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="receiver_id")
    private Member receiver;

    public Notification( Member receiver) {
        this.isRead = false;
        this.receiver = receiver;
    }
}
