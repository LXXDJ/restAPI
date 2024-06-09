package com.ohgiraffers.comprehensive.order.presentation;

import com.ohgiraffers.comprehensive.auth.type.CustomUser;
import com.ohgiraffers.comprehensive.common.paging.Pagenation;
import com.ohgiraffers.comprehensive.common.paging.PagingButtonInfo;
import com.ohgiraffers.comprehensive.common.paging.PagingResponse;
import com.ohgiraffers.comprehensive.order.dto.request.OrderCreateRequest;
import com.ohgiraffers.comprehensive.order.dto.response.OrdersResponse;
import com.ohgiraffers.comprehensive.order.service.OrderService;
import com.ohgiraffers.comprehensive.review.dto.response.ReviewsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /* 주문 등록 */
    @PostMapping("/orders")
    public ResponseEntity<Void> save(
            @RequestBody @Valid final OrderCreateRequest orderRequest,
            @AuthenticationPrincipal final CustomUser customUser
            ) {

        orderService.save(orderRequest, customUser.getMemberCode());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /* 회원의 주문 목록 조회 */
    @GetMapping("/members/{memberId}/orders")
    @PreAuthorize("#memberId == authentication.principal.username")
    public ResponseEntity<PagingResponse> getOrders(
            @RequestParam(defaultValue = "1") final Integer page,
            @PathVariable String memberId,
            @AuthenticationPrincipal CustomUser customUser
    ) {
        final Page<OrdersResponse> orders = orderService.getOrders(page, customUser.getMemberCode());
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(orders);
        final PagingResponse pagingResponse = PagingResponse.of(orders.getContent(), pagingButtonInfo);
        return ResponseEntity.ok(pagingResponse);
    }




}
