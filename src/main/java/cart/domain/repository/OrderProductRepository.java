package cart.domain.repository;

import cart.domain.Order;
import org.springframework.stereotype.Component;

@Component
public interface OrderProductRepository {

    void save(Long orderSavedId, Order order);
}
