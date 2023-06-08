package cart.repository;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.OrderInfo;

import java.util.List;
import java.util.Map;

public interface OrderRepository {

    long save(final Order order);
    List<OrderInfo> findByMember(final Member member);
    Map<Long, List<Product>> findProductsByIds(final List<Long> ids);
    Order findById(long id);
}
