package cart.domain.repository;

import cart.domain.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface OrderProductRepository {

    void saveOrderProductsByOrderId(Long orderSavedId, Order order);
}
