package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Order;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MysqlOrderRepository implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final CartItemDao cartItemDao;

    public MysqlOrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao, final CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.cartItemDao = cartItemDao;
    }

    @Override
    public Order save(final Order order) {
        final OrderEntity savedOrder = orderDao.createOrder(new OrderEntity(order.getId(), order.getPrice()));
        final List<OrderItemEntity> orderItemEntities = order.getCartItems().stream()
                .map(cartItem -> new OrderItemEntity(savedOrder.getId(), cartItem.getProductId(), cartItem.getQuantity()))
                .collect(Collectors.toList());

        orderItemDao.saveAll(orderItemEntities);
        cartItemDao.deleteAllById(order.getCartItemIds());

        return new Order(savedOrder.getId(), order.getPrice(), order.getMember(), order.getCartItems());
    }
}
