package cart.domain.repository;

import cart.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository {

    void saveOrderProductsByOrderId(Long orderSavedId, Order order);
}
