package com.ohgiraffers.comprehensive.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OrderCreateRequest {

    @Min(value = 1)
    private final Long productCode;
    @NotBlank
    private final String orderPhone;
    @NotBlank
    private final String orderEmail;
    @NotBlank
    private final String orderReceiver;
    @NotBlank
    private final String orderAddress;
    @Min(value = 1)
    private final Long orderAmount;
}
