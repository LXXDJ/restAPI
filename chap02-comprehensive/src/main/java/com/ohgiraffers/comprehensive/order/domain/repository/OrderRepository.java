package com.ohgiraffers.comprehensive.order.domain.repository;

import com.ohgiraffers.comprehensive.order.domain.entity.Order;
import com.ohgiraffers.comprehensive.order.dto.response.OrdersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderCodeAndMemberCode(Long orderCode, Long memberCode);

    @Query("select new com.ohgiraffers.comprehensive.order.dto.response.OrdersResponse(o, p, r) "+
            "from Order o join Product p on o.productCode = p.productCode " +
            "left join Review r on o.orderCode = r.orderCode " +
            "where o.memberCode = :memberCode")
    Page<OrdersResponse> findByMemberCode(Pageable pageable, Long memberCode);
}
