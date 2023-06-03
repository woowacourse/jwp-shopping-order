package cart.application;

import cart.dao.OrderDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderCalculator;
import cart.dto.OrderRequest;
import cart.exception.OrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final CartItemService cartItemService;
    private final OrderDao orderDao;
    private final OrderCalculator orderCalculator;

    public OrderService(CartItemService cartItemService, OrderDao orderDao, OrderCalculator orderCalculator) {
        this.cartItemService = cartItemService;
        this.orderDao = orderDao;
        this.orderCalculator = orderCalculator;
    }

    @Transactional
    public Long add(Member member, OrderRequest orderRequest) {
        final List<CartItem> cartItems = cartItemService.findByIds(orderRequest.getCartItems());
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));

        final Order order = Order.of(cartItems, member, orderRequest.getPaymentAmount());
        orderCalculator.checkPaymentAmount(order, orderRequest.getPaymentAmount());

        cartItemService.remove(member, orderRequest.getCartItems());
        return orderDao.save(order);
    }

    @Transactional(readOnly = true)
    public Order findById(Member member, Long orderId) {
        final Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new OrderException.NoSuchId(orderId));
        order.checkOwner(member);

        return order;
    }

    public List<Order> findAllByMember(Member member) {
        return orderDao.findAllByMember(member);
    }
}
