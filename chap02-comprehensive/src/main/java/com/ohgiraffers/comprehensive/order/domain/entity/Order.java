package com.ohgiraffers.comprehensive.order.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderCode;
    private Long productCode;
    private Long memberCode;
    private String orderPhone;
    private String orderEmail;
    private String orderReceiver;
    private String orderAddress;
    private Long orderAmount;
    @CreatedDate
    private LocalDateTime orderDate;

    private Order(Long productCode, Long memberCode, String orderPhone, String orderEmail, String orderReceiver, String orderAddress, Long orderAmount) {
        this.productCode = productCode;
        this.memberCode = memberCode;
        this.orderPhone = orderPhone;
        this.orderEmail = orderEmail;
        this.orderReceiver = orderReceiver;
        this.orderAddress = orderAddress;
        this.orderAmount = orderAmount;
    }

    public static Order of(Long productCode, Long memberCode, String orderPhone, String orderEmail, String orderReceiver, String orderAddress, Long orderAmount) {
        return new Order(productCode, memberCode, orderPhone, orderEmail, orderReceiver, orderAddress, orderAmount);
    }
}
