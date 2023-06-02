package cart.repository;

import java.util.List;
import java.util.stream.Collectors;

import cart.dao.OrderDetailDao;
import cart.dao.OrdersDao;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderPoint;
import cart.domain.Point;
import cart.domain.Product;
import cart.entity.OrderDetailEntity;
import cart.entity.OrdersEntity;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrdersDao ordersDao;
    private final OrderDetailDao orderDetailDao;

    public OrderRepository(final OrdersDao ordersDao, final OrderDetailDao orderDetailDao) {
        this.ordersDao = ordersDao;
        this.orderDetailDao = orderDetailDao;
    }

    public Order createOrder(final Member member, final Order order) {
        final Long orderId = insertOrder(member, order);
        insertOrderDetail(orderId, order.getCartItems());
        return new Order(orderId, order);
    }

    private Long insertOrder(final Member member, final Order order) {
        final OrdersEntity ordersEntity = new OrdersEntity(
                member.getId(),
                order.getOrderPoint().getPointId(),
                order.getOrderPoint().getEarnedPoint().getValue(),
                order.getOrderPoint().getUsedPoint().getValue(),
                order.getCreatedAt());
        return ordersDao.insert(ordersEntity).getId();
    }

    private void insertOrderDetail(final Long orderId, final CartItems cartItems) {
        cartItems.getCartItems().stream()
                .map(cartItem -> OrderDetailEntity.builder()
                        .ordersId(orderId)
                        .productId(cartItem.getProduct().getId())
                        .productName(cartItem.getProduct().getName())
                        .productPrice(cartItem.getProduct().getPrice())
                        .productImageUrl(cartItem.getProduct().getImageUrl())
                        .orderQuantity(cartItem.getQuantity())
                        .build())
                .forEach(orderDetailDao::insert);
    }

    public List<Order> findByMember(final Member member) {
        final List<OrdersEntity> ordersEntities = ordersDao.findByMemberId(member.getId());
        return ordersEntities.stream()
                .map(this::getOrderByOrdersEntity)
                .collect(Collectors.toList());
    }

    private Order getOrderByOrdersEntity(final OrdersEntity ordersEntity) {
        return new Order(
                ordersEntity.getId(),
                getCartItemsByOrderId(ordersEntity.getId()),
                getOrderPointByOrdersEntity(ordersEntity),
                ordersEntity.getCreatedAt()
        );
    }

    private CartItems getCartItemsByOrderId(final Long orderId) {
        final List<OrderDetailEntity> orderDetailEntities = orderDetailDao.findByOrderId(orderId);
        return CartItems.of(orderDetailEntities.stream()
                .map(this::getCartItemByOrderDetailEntity)
                .collect(Collectors.toList()));
    }

    private CartItem getCartItemByOrderDetailEntity(final OrderDetailEntity orderDetailEntity) {
        return new CartItem(new Product(
                orderDetailEntity.getProductId(),
                orderDetailEntity.getProductName(),
                orderDetailEntity.getProductPrice(),
                orderDetailEntity.getProductImageUrl()
        ), orderDetailEntity.getOrderQuantity());
    }

    private OrderPoint getOrderPointByOrdersEntity(final OrdersEntity ordersEntity) {
        return new OrderPoint(
                ordersEntity.getPointId(),
                Point.valueOf(ordersEntity.getUsedPoint()),
                Point.valueOf(ordersEntity.getEarnedPoint())
        );
    }
}
