package cart.repository;

import cart.dao.OrderDetailDao;
import cart.dao.OrdersDao;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;
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
}
