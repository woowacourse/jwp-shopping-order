package cart.order.infrastructure.persistence.repository;

import static cart.order.exception.OrderExceptionType.NOT_FOUND_ORDER;

import cart.order.domain.Order;
import cart.order.domain.OrderRepository;
import cart.order.exception.OrderException;
import cart.order.infrastructure.persistence.dao.OrderDao;
import cart.order.infrastructure.persistence.dao.OrderItemDao;
import cart.order.infrastructure.persistence.entity.OrderEntity;
import cart.order.infrastructure.persistence.entity.OrderItemEntity;
import cart.order.infrastructure.persistence.mapper.OrderEntityMapper;
import cart.order.infrastructure.persistence.mapper.OrderItemEntityMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public JdbcOrderRepository(OrderDao orderDao, OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public Long save(Order order) {
        OrderEntity orderEntity = OrderEntityMapper.toEntity(order);
        Long orderId = orderDao.save(orderEntity);
        List<OrderItemEntity> orderItemEntities = OrderItemEntityMapper.toEntities(
                order.getOrderItems(), orderId
        );
        orderItemDao.saveAll(orderItemEntities);
        return orderId;
    }

    @Override
    public Order findById(Long id) {
        OrderEntity orderEntity = orderDao.findById(id)
                .orElseThrow(() -> new OrderException(NOT_FOUND_ORDER));
        List<OrderItemEntity> orderItemEntities = orderItemDao.findAllByOrderId(id);
        return OrderEntityMapper.toDomain(orderEntity, orderItemEntities);
    }

    @Override
    public List<Order> findAllByMemberId(Long memberId) {
        return orderDao.findAllByMemberId(memberId).stream()
                .map(it ->
                        OrderEntityMapper.toDomain(it,
                                orderItemDao.findAllByOrderId(it.getId()))
                ).collect(Collectors.toList());
    }
}
