package cart.application;

import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.order.DiscountPolicy;
import cart.domain.order.FixedDiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItems;
import cart.dto.OrderCreateRequest;
import cart.repository.OrderRepository;
import cart.repository.dao.CartItemDao;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final CartItemDao cartItemDao;
    private final OrderRepository orderRepository;

    public OrderService(final CartItemDao cartItemDao, final OrderRepository orderRepository) {
        this.cartItemDao = cartItemDao;
        this.orderRepository = orderRepository;
    }

    public Long order(final Member member, final OrderCreateRequest orderCreateRequest) {
        final List<CartItem> findCartItems = cartItemDao.findByIds(orderCreateRequest.getCartItemIds());
        final CartItems cartItems = new CartItems(findCartItems, member);

        final OrderItems orderItems = OrderItems.from(cartItems);
        final DiscountPolicy discountPolicy = FixedDiscountPolicy.from(orderItems.sumOfPrice());
        final Order order = new Order(member, orderItems, discountPolicy);

        final Long orderId = orderRepository.createOrder(order);
        cartItemDao.deleteByIds(orderCreateRequest.getCartItemIds());

        return orderId;
    }
}
