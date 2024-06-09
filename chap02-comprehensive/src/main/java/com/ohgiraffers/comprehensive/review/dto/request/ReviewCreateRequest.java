package com.ohgiraffers.comprehensive.review.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewCreateRequest {

    @Min(value = 1)
    private final Long orderCode;
    @NotBlank
    private final String reviewTitle;
    @NotBlank
    private final String reviewContent;
}
