package com.ohgiraffers.comprehensive.product.domain.repository;

import com.ohgiraffers.comprehensive.product.domain.entity.Product;
import com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /* 1. 상품 목록 조회 : 페이징, 주문 불가 상품 제외 (고객) */
    Page<Product> findByStatus(Pageable pageable, ProductStatusType productStatusType);
    /* 2. 상품 목록 조회 : 페이징, 주문 불가 상품 포함 (관리자) */
    @EntityGraph(attributePaths = {"category"})
    Page<Product> findByStatusNot(Pageable pageable, ProductStatusType productStatusType);
    /* 3. 상품 목록 조회 : 카테고리 기준, 페이징, 주문 불가 상품 제외 (고객) */
    Page<Product> findByCategoryCategoryCodeAndStatus(Pageable pageable, Long categoryCode, ProductStatusType productStatusType);
    /* 4. 상품 목록 조회 : 상품명 기준, 페이징, 주문 불가 상품 제외 (고객) */
    Page<Product> findByProductNameContainsAndStatus(Pageable pageable, String productName, ProductStatusType productStatus);
    /* 5. 상품 상세 조회 : productCode로 상품 1개 조회, 주문 불가 상품 제외 (고객) */
    Optional<Product> findByProductCodeAndStatus(Long productCode, ProductStatusType productStatusType);
    /* 6. 상품 상세 조회 : productCode로 상품 1개 조회, 주문 불가 상품 포함 (관리자) */
    @EntityGraph(attributePaths = {"category"})
    Optional<Product> findByProductCodeAndStatusNot(Long productCode, ProductStatusType productStatusType);












}
