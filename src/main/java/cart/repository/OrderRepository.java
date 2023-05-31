package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.domain.order.Order;
import cart.exception.OrderException.NotFound;
import cart.repository.mapper.OrderMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;

    public OrderRepository(OrderDao orderDao, OrderProductDao orderProductDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
    }

    public List<Order> findAllByMemberId(Long memberId) {
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(memberId);
        return orderEntities.stream()
                .map(orderEntity -> {
                    List<OrderProductEntity> orderProductEntities = orderProductDao.findAllByOrderId(orderEntity.getId());
                    return OrderMapper.toDomain(orderEntity, orderProductEntities);
                })
                .collect(Collectors.toList());
    }

    public Order findById(Long id) {
        OrderEntity orderEntity = orderDao.findById(id).orElseThrow(NotFound::new);
        List<OrderProductEntity> orderProductEntities = orderProductDao.findAllByOrderId(id);
        return OrderMapper.toDomain(orderEntity, orderProductEntities);
    }
}
