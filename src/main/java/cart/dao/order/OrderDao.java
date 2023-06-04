package cart.dao.order;

import cart.entity.OrderEntity;

import java.util.List;

public interface OrderDao {
    Long insert(OrderEntity orderEntity);

    OrderEntity findById(Long id);

    List<OrderEntity> findAll(Long memberId);

}
