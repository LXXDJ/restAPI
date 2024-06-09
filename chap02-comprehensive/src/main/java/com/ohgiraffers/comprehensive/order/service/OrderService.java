package com.ohgiraffers.comprehensive.order.service;

import com.ohgiraffers.comprehensive.common.exception.NotFoundException;
import com.ohgiraffers.comprehensive.order.domain.entity.Order;
import com.ohgiraffers.comprehensive.order.domain.repository.OrderRepository;
import com.ohgiraffers.comprehensive.order.dto.request.OrderCreateRequest;
import com.ohgiraffers.comprehensive.order.dto.response.OrdersResponse;
import com.ohgiraffers.comprehensive.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.NOT_FOUND_VALID_ORDER;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    public void save(OrderCreateRequest orderRequest, Long memberCode) {

        updateStock(orderRequest.getProductCode(), orderRequest.getOrderAmount());

        final Order newOrder = Order.of(
                orderRequest.getProductCode(),
                memberCode,
                orderRequest.getOrderPhone(),
                orderRequest.getOrderEmail(),
                orderRequest.getOrderReceiver(),
                orderRequest.getOrderAddress(),
                orderRequest.getOrderAmount()
        );

        orderRepository.save(newOrder);
    }

    private void updateStock(Long productCode, Long orderAmount) {
        productService.updateStock(productCode, orderAmount);
    }

    public void verifyOrdered(Long orderCode, Long memberCode) {
        if(!orderRepository.existsByOrderCodeAndMemberCode(orderCode, memberCode))
            throw new NotFoundException(NOT_FOUND_VALID_ORDER);
    }
    private Pageable getPageable(Integer page) {
        return PageRequest.of(page - 1, 10, Sort.by("orderCode").descending());
    }

    @Transactional(readOnly = true)
    public Page<OrdersResponse> getOrders(Integer page, Long memberCode) {

        return orderRepository.findByMemberCode(getPageable(page), memberCode);
    }
}
