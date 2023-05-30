package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.OrderItem;
import cart.dto.OrderRequest;
import cart.exception.CartItemException.IllegalId;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Money DELIVERY_FEE_BASIC = new Money(3000L);
    private final OrderDao orderDao;
    private final CartItemDao cartItemDao;

    public OrderService(final OrderDao orderDao, final CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
    }

    public Long add(final Member member, final OrderRequest orderRequest) {
        final Long orderId = orderDao.createOrder(member.getId(), DELIVERY_FEE_BASIC);
        orderDao.addOrderItems(createOrderItems(orderId, member, orderRequest));
        return orderId;
    }

    private List<OrderItem> createOrderItems(final Long orderId, final Member member, final OrderRequest orderRequest) {
        final List<Long> cartItemIds = orderRequest.getCartItemIds();
        final List<CartItem> cartItems = cartItemDao.findByIds(cartItemIds);
        validateIds(member, cartItemIds, cartItems);
        return OrderItem.from(orderId, cartItems);
    }

    private void validateIds(final Member member, final List<Long> cartItemIds, final List<CartItem> cartItems) {
        for (final Long cartItemId : cartItemIds) {
            validateId(member, cartItemId, cartItems);
        }
    }

    private void validateId(final Member member, final Long cartItemId, final List<CartItem> cartItems) {
        final CartItem cartItem = cartItems.stream()
                .filter(item -> Objects.equals(item.getId(), cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalId(cartItemId));
        cartItem.checkOwner(member);
    }
}
