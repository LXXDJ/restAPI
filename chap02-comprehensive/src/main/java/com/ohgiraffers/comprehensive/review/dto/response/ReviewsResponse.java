package com.ohgiraffers.comprehensive.review.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ohgiraffers.comprehensive.member.domain.entity.Member;
import com.ohgiraffers.comprehensive.product.domain.entity.Product;
import com.ohgiraffers.comprehensive.review.domain.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewsResponse {

    private Long reviewCode;
    private String productName;
    private String memberName;
    private String reviewTitle;
    private String reviewContent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public ReviewsResponse(Review review, Product product, Member member) {
        this.reviewCode = review.getReviewCode();
        this.productName = product.getProductName();
        this.memberName = member.getMemberName();
        this.reviewTitle = review.getReviewTitle();
        this.reviewContent = review.getReviewContent();
        this.createdAt = review.getCreatedAt();
    }
}
