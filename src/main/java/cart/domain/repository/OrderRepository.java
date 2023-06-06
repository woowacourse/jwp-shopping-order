package cart.domain.repository;

import cart.domain.Order;
import cart.domain.coupon.Coupon;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderRepository {
    Order save(Order order);

    List<Order> findAllByMemberId(Long memberId);

    Order findByIdAndMemberId(Long orderId, Long memberId);

    void deleteById(Long orderId, Long memberId);

    Coupon confirmById(Long orderId, Long memberId);

    boolean existsConfirmStateById(Long orderId);
}
