package cart.repository;

import cart.domain.Member;
import cart.domain.Order;

public interface OrderRepository {

    Order save(final Order order);

    Order findById(final Long id, final Member member);
}
