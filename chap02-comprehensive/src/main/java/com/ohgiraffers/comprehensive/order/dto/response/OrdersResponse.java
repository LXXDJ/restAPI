package com.ohgiraffers.comprehensive.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ohgiraffers.comprehensive.order.domain.entity.Order;
import com.ohgiraffers.comprehensive.product.domain.entity.Product;
import com.ohgiraffers.comprehensive.review.domain.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class OrdersResponse {

    private Long orderCode;
    private Long productCode;
    private String productName;
    private Long productPrice;
    private Long orderAmount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    private Long reviewCode;
    private String reviewTitle;
    private String reviewContent;

    public OrdersResponse(Order order, Product product, Review review) {
        this.orderCode = order.getOrderCode();
        this.productCode = order.getProductCode();
        this.productName = product.getProductName();
        this.productPrice = product.getProductPrice();
        this.orderAmount = order.getOrderAmount();
        this.orderDate = order.getOrderDate();
        if(review != null){
            this.reviewCode = review.getReviewCode();
            this.reviewTitle = review.getReviewTitle();
            this.reviewContent = review.getReviewContent();
        }
    }








}
