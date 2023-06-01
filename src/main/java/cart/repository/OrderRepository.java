package cart.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    List<Order> findAllByMemberId(Long memberId);

    Optional<Order> findById(Long id);

    Long create(Member member, Integer usedPoint, List<CartItem> cartItems);
}
