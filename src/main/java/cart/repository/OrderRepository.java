package cart.repository;

import cart.dao.cart.CartItemDao;
import cart.dao.order.OrderDao;
import cart.dao.order.OrderItemDao;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;


    public OrderRepository(final CartItemDao cartItemDao, OrderDao orderDao, OrderItemDao orderItemDao) {
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public Order makeOrder(final Member member, final List<Long> cartItemIds) {
        List<CartItem> cartItems = cartItemDao.findByIds(cartItemIds);
        List<OrderItem> orderItems = cartItemToOrderItems(cartItems);
        return new Order(member, orderItems);
    }

    public Long save(final Order order, final List<Long> cartItemIds) {
        cartItemDao.deleteByIds(cartItemIds);
        Long orderId = orderDao.insertOrder(order);
        orderItemDao.insert(order.getOrderItems(), orderId);
        return orderId;
    }

    private static List<OrderItem> cartItemToOrderItems(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem(cartItem.getProduct().getName(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getProduct().getImageUrl(),
                        cartItem.getQuantity(),
                        cartItem.getProduct().getDiscountRate()))
                .collect(Collectors.toList());
    }
}
