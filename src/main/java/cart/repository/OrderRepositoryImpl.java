package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.PaymentDao;
import cart.domain.CartItems;
import cart.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderDao orderDao;
    private final PaymentDao paymentDao;
    private final OrderItemDao orderItemDao;
    private final CartItemDao cartItemDao;

    public OrderRepositoryImpl(final OrderDao orderDao, final PaymentDao paymentDao, final OrderItemDao orderItemDao,
                               final CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.paymentDao = paymentDao;
        this.orderItemDao = orderItemDao;
        this.cartItemDao = cartItemDao;
    }

    public Long createOrder(final Order order, final CartItems cartItems) {
        Long orderId = orderDao.save(order);
        paymentDao.save(order.getPayment(), orderId, order.getMember().getId());
        orderItemDao.saveAll(order.getOrderItems(), orderId);
        cartItemDao.deleteAll(cartItems);
        return orderId;
    }

    // TODO: 2023/06/01 member 검증
    public CartItems findCartItemsByMemberId(Long memberId) {
        return cartItemDao.findByMemberId(memberId);
    }
}
