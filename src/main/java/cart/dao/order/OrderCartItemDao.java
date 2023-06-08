package cart.dao.order;

import cart.entity.OrderCartItemEntity;

import java.util.List;

public interface OrderCartItemDao {

    Long insert(OrderCartItemEntity orderCartItemEntity);

    void insertAll(List<OrderCartItemEntity> orderCartItemEntities);

    List<OrderCartItemEntity> findByOrderId(Long orderId);

}
