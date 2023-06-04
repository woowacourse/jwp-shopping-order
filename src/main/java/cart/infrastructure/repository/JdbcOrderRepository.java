package cart.infrastructure.repository;

import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import cart.domain.repository.OrderRepository;
import cart.entity.OrderEntity;
import cart.entity.ProductOrderEntity;
import cart.infrastructure.dao.OrderDao;
import cart.infrastructure.dao.ProductOrderDao;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final OrderDao orderDao;
    private final ProductOrderDao productOrderDao;

    public JdbcOrderRepository(final OrderDao orderDao, final ProductOrderDao productOrderDao) {
        this.orderDao = orderDao;
        this.productOrderDao = productOrderDao;
    }

    @Override
    public Order create(final Order order, final Long memberId) {
        final Long orderId = orderDao.create(OrderEntity.of(order, memberId), memberId);
        final List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            productOrderDao.create(ProductOrderEntity.of(orderItem, orderId));
        }
        return new Order(orderId, order.getOrderItems(), order.getCoupon(), order.getTotalProductAmount(),
                order.getDeliveryAmount(), order.getAddress());
    }
}
