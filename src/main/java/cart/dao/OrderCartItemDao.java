package cart.dao;

import cart.domain.OrderCartItemEntity;

public interface OrderCartItemDao {

    Long insert(OrderCartItemEntity orderCartItemEntity);

    OrderCartItemEntity findById(Long id);

}
