package cart.dao;

import cart.domain.Member;
import cart.domain.OrderEntity;

import java.util.List;

public interface OrderDao {
    Long insert(OrderEntity orderEntity);

    OrderEntity findById(Long id);

    List<OrderEntity> findAll(Long memberId);

}
