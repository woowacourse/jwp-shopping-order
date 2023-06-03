package cart.dao;

import cart.domain.OrderEntity;

public interface OrderDao {
    Long insert(OrderEntity orderEntity);

    OrderEntity findById(Long id);

}
