package cart.dao;

import cart.domain.OrderCartItemEntity;

import java.util.List;

public interface OrderCartItemDao {

    Long insert(OrderCartItemEntity orderCartItemEntity);

    OrderCartItemEntity findById(Long id);

    List<OrderCartItemEntity> findByOrderId(Long orderId);

}
