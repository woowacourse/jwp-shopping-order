package shop.domain.repository;

import shop.domain.member.Member;
import shop.domain.order.Order;
import shop.domain.order.OrderDetail;

import java.util.List;

public interface OrderRepository {
    Long save(Long memberId, Long couponId, Order order);

    OrderDetail findDetailsByMemberAndOrderId(Member member, Long orderId);

    List<Order> findAllByMember(Member member);
}
