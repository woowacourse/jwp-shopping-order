package shop.domain.repository;

import shop.application.order.dto.OrderDetailDto;
import shop.domain.member.Member;
import shop.domain.order.Order;

import java.util.List;

public interface OrderRepository {
    Long save(Long memberId, Long couponId, Order order);

    OrderDetailDto findDetailsByMemberAndOrderId(Member member, Long orderId);

    List<Order> findAllByMember(Member member);
}
