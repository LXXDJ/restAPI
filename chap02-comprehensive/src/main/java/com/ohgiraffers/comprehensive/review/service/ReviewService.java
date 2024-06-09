package com.ohgiraffers.comprehensive.review.service;

import com.ohgiraffers.comprehensive.common.exception.ConflictException;
import com.ohgiraffers.comprehensive.order.service.OrderService;
import com.ohgiraffers.comprehensive.review.domain.entity.Review;
import com.ohgiraffers.comprehensive.review.domain.repository.ReviewRepository;
import com.ohgiraffers.comprehensive.review.dto.request.ReviewCreateRequest;
import com.ohgiraffers.comprehensive.review.dto.response.ReviewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ohgiraffers.comprehensive.common.domain.type.StatusType.USABLE;
import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.ALREADY_EXIST_REVIEW;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final OrderService orderService;
    private final ReviewRepository reviewRepository;

    public void save(ReviewCreateRequest reviewRequest, Long memberCode) {

        verifyOrdered(reviewRequest.getOrderCode(), memberCode);
        verifyReviewNotCreated(reviewRequest.getOrderCode());

        Review newReview = Review.of(
                reviewRequest.getOrderCode(),
                reviewRequest.getReviewTitle(),
                reviewRequest.getReviewContent()
        );

        reviewRepository.save(newReview);
    }

    private void verifyReviewNotCreated(Long orderCode) {
        if(reviewRepository.existsByOrderCodeAndStatus(orderCode, USABLE)) {
            throw new ConflictException(ALREADY_EXIST_REVIEW);
        }
    }

    private void verifyOrdered(Long orderCode, Long memberCode) {
        orderService.verifyOrdered(orderCode, memberCode);
    }

    /* Review에 Order를 연관 관계 매핑하면?
    * 1. Review를 통해 Order를 다룰 수 있게 되면 Order의 상태를 변경하는 오류를 범할 수 있다.
    *  - 편리함의 오용(각 aggregate 간의 의존 결합도 높아짐)
    * 2. 성능 이슈 (Review -> Order -> Member & Product -> Category)
    *  - 즉시 로딩 : 연관 관계 객체 조회 시 유리
    *  - 지연 로딩 : 상태 변경 기능 시 유리(불필요한 객체 로딩이 필요 없음)
    *  - 다양한 경우의 수를 고려해서 연관 관계 매핑 및 로딩 전략을 결정해야 함
    * 3. 확장성
    *  - 초기에는 단일 서버, 단일 DBMS 사용
    *  - 규모가 커지면 하위 도메인별로 시스템을 분리할 수 있음. DBMS도 달라질 수 있음.
    * => 결론 : ID를 이용한 간접 참조를 통해 위의 문제를 완화
    * => 다만 조회 시에는 간접 참조가 성능적인 문제를 야기한다. -> N + 1 문제와 유사한 상황
    * => 조회 전용 쿼리를 사용해서 한 번의 쿼리로 필요한 데이터를 로딩한다.
    *    쿼리가 복잡하거나 SQL에 특화 된 기능을 사용해야 한다면 조회만 MyBatis를 사용하는 경우도 있다.
    * */
    @Transactional(readOnly = true)
    public Page<ReviewsResponse> getReviews(Integer page, Long productCode) {

        return reviewRepository.findByProductCode(getPageable(page), productCode);
    }

    private Pageable getPageable(Integer page) {
        return PageRequest.of(page - 1, 5, Sort.by("reviewCode").descending());
    }
}
