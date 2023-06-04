package cart.domain.repository;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.coupon.Coupon;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderRepository {
    Long save(Order order);

    List<Order> findAllByMemberId(Member member);

    Order findByIdAndMemberId(Member member, Long orderId);

    void deleteById(Member member, Long orderId);

    Coupon confirmById(Long orderId, Member member);

    boolean checkConfirmStateById(Long orderId);
}
