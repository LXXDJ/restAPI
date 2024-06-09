package com.ohgiraffers.comprehensive.product.dto.response;

import com.ohgiraffers.comprehensive.product.domain.entity.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerProductsResponse {

    private final Long productCode;
    private final String productName;
    private final Long productPrice;
    private final String productImageUrl;

    public static CustomerProductsResponse from(final Product product) {
        return new CustomerProductsResponse(
                product.getProductCode(),
                product.getProductName(),
                product.getProductPrice(),
                product.getProductImageUrl()
        );
    }

}
