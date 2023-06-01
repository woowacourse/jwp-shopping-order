package cart.step2.order.persist;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.step2.order.domain.Order;
import cart.step2.order.domain.OrderEntity;
import cart.step2.order.domain.OrderItem;
import cart.step2.order.domain.OrderItemEntity;
import cart.step2.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepositoryMapper implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final ProductDao productDao;

    public OrderRepositoryMapper(final OrderDao orderDao, final OrderItemDao orderItemDao, final ProductDao productDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.productDao = productDao;
    }

    @Override
    public Long save(final Order order) {
        OrderEntity orderEntity = OrderEntity.createNonePkOrder(order.getPrice(), order.getCouponId(), order.getMemberId());
        return orderDao.insert(orderEntity);
    }

    @Override
    public List<Order> findAllByMemberId(final Long memberId) {
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(memberId);
        List<Order> orders = new ArrayList<>();
        for (final OrderEntity orderEntity : orderEntities) {
            List<OrderItem> orderItems = getOrderItems(orderEntity);
            Order order = Order.of(orderEntity.getId(), orderEntity.getPrice(), orderEntity.getCouponId(), orderEntity.getMemberId(), orderEntity.getDate(), orderItems);
            orders.add(order);
        }
        return orders;
    }

    private List<OrderItem> getOrderItems(final OrderEntity orderEntity) {
        List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(orderEntity.getId());
        List<OrderItem> orderItems = new ArrayList<>();
        for (final OrderItemEntity orderItemEntity : orderItemEntities) {
            Product product = productDao.getProductById(orderItemEntity.getProductId());
            OrderItem orderItem = OrderItem.of(orderItemEntity.getId(), product, orderItemEntity.getQuantity());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

}
