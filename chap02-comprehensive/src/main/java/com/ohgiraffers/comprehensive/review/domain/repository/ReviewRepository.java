package com.ohgiraffers.comprehensive.review.domain.repository;

import com.ohgiraffers.comprehensive.common.domain.type.StatusType;
import com.ohgiraffers.comprehensive.review.domain.entity.Review;
import com.ohgiraffers.comprehensive.review.dto.response.ReviewsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository <Review, Long>{
    boolean existsByOrderCodeAndStatus(Long orderCode, StatusType statusType);

    @Query(
            "select new com.ohgiraffers.comprehensive.review.dto.response.ReviewsResponse(r, p, m) " +
                    "from Review r join Order o on r.orderCode = o.orderCode " +
                    "join Product p on p.productCode = o.productCode " +
                    "join Member m on m.memberCode = o.memberCode " +
                    "where p.productCode = :productCode " +
                    "and r.status ='USABLE'"
    )
    Page<ReviewsResponse> findByProductCode(Pageable pageable, Long productCode);
}
