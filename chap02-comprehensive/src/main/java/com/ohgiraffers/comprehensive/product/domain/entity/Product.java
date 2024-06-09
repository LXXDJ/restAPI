package com.ohgiraffers.comprehensive.product.domain.entity;

import com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tbl_product SET status = 'DELETED' WHERE product_code = ?")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productCode;
    private String productName;
    private Long productPrice;
    private String productDescription;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryCode")
    private Category category;
    private String productImageUrl;
    private Long productStock;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    @Enumerated(value = EnumType.STRING)
    private ProductStatusType status = ProductStatusType.USABLE;

    private Product(
            String productName, Long productPrice, String productDescription,
            Category category, String productImageUrl, Long productStock
    ) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.category = category;
        this.productImageUrl = productImageUrl;
        this.productStock = productStock;
    }

    public static Product of(
            final String productName, final Long productPrice, final String productDescription,
            final Category category, final String productImageUrl, final Long productStock
    ) {
        return new Product(
                productName,
                productPrice,
                productDescription,
                category,
                productImageUrl,
                productStock
        );
    }

    public void modify(String productName, Long productPrice, String productDescription, Category category, Long productStock, ProductStatusType status) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.category = category;
        this.productStock = productStock;
        this.status = status;
    }

    public void modifyProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public void changeStock(Long orderAmount) {
        this.productStock -= orderAmount;
    }
}
