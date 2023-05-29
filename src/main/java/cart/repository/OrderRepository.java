package cart.repository;

import cart.domain.Member;
import cart.domain.Product;
import cart.domain.order.DiscountPolicy;
import cart.domain.order.FixedDiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItems;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.repository.dao.OrderDao;
import cart.repository.dao.OrderItemDao;
import cart.repository.dao.ProductDao;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final ProductDao productDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao, final ProductDao productDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.productDao = productDao;
    }

    public Long createOrder(Order order) {
        final OrderEntity orderEntity = OrderEntity.from(order);
        final Long orderId = orderDao.createOrder(orderEntity);

        final List<OrderItemEntity> orderItems = OrderItemEntity.from(orderId, order.getOrderItems());
        orderItemDao.saveAll(orderItems);

        return orderId;
    }

    public Optional<Order> findById(final Member member, final Long orderId) {
        Optional<OrderEntity> findOrder = orderDao.findById(orderId);

        final List<OrderItemEntity> findOrderItems = orderItemDao.getOrderItemsByOrderId(orderId);
        final List<Long> productIds = findOrderItems.stream()
                .map(OrderItemEntity::getId)
                .collect(Collectors.toUnmodifiableList());

        final List<Product> findProducts = productDao.getProductByIds(productIds);

        final OrderItems orderItems = OrderItems.of(findOrderItems, findProducts, member);
        final DiscountPolicy discountPolicy = FixedDiscountPolicy.from(orderItems.sumOfPrice());

        return findOrder.map(orderEntity -> orderEntity.toDomain(member, orderItems, discountPolicy));
    }
}
