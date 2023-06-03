package cart.repository;

import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.member.Member;
import cart.entity.OrderDetailEntity;
import cart.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final ProductDao productDao;

    public OrderRepository(final OrderDao orderDao, final ProductDao productDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    public Long addOrder(final Order order) {
        final Long orderId = orderDao.addOrder(new OrderEntity(order.getMemberId(), order.getPaymentAmount(), order.getPointAmount()));

        final List<OrderItem> orderItems = order.getOrderItems();
        orderItems.stream()
                .map(orderItem -> new OrderDetailEntity(orderId, orderItem.getProductId(), orderItem.getQuantity()))
                .forEach(orderDao::addOrderDetail);

        return orderId;
    }

    public Order getOrderById(final Member member, final Long orderId) {
        final OrderEntity orderEntity = orderDao.getOrderEntityById(orderId);
        return generateOrder(member, orderEntity);
    }

    private Order generateOrder(final Member member, final OrderEntity orderEntity) {
        final List<OrderDetailEntity> detailEntities = orderDao.getOrderDetailEntitiesByOrderId(orderEntity.getId());
        return Order.from(orderEntity.getId(), member, orderEntity.getPayment(), orderEntity.getDiscountPoint(), generateOrderItems(detailEntities));
    }

    private List<OrderItem> generateOrderItems(final List<OrderDetailEntity> detailEntities) {
        return detailEntities.stream()
                .map(it -> new OrderItem(productDao.getProductById(it.getProductId()), it.getQuantity()))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Order> getAllOrders(final Member member, final int start, final int size) {
        final List<OrderEntity> orderEntities = orderDao.getOrderEntityByMemberId(member.getId(), start, size);
        return orderEntities.stream()
                .map(orderEntity -> generateOrder(member, orderEntity))
                .collect(Collectors.toUnmodifiableList());
    }

    public int countAllOrders() {
        return orderDao.countAllOrders();
    }
}
