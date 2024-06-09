package com.ohgiraffers.comprehensive.product.dto.request;

import com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductUpdateRequest {

    @NotBlank
    private final String productName;
    @Min(value = 1)
    private final Long productPrice;
    @NotBlank
    private final String productDescription;
    @Min(value = 1)
    private final Long categoryCode;
    @Min(value = 1)
    private final Long productStock;
    @NotNull
    private final ProductStatusType status;

}
