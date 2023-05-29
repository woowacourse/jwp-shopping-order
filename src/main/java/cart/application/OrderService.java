package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;

    public OrderService(final CartItemDao cartItemDao, final OrderDao orderDao) {
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
    }

    public Long order(final Member member, final OrderRequest orderRequest) {
        final List<CartItem> cartItems = cartItemDao.findAllByIds(orderRequest.getCartItemIds());
        validateMember(member, cartItems);

        final Order order = new Order(member, 0, cartItems);
        final Long orderId = orderDao.save(order).getId();

        cartItemDao.deleteAllByIds(cartItemsToIds(cartItems));
        return orderId;
    }

    private List<Long> cartItemsToIds(final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(toList());
    }

    private void validateMember(final Member member, final List<CartItem> cartItems) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
    }

    public List<OrderResponse> findAllByMember(final Member member) {
        final List<Order> orders = orderDao.findAllByMemberId(member.getId());
        return orders.stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    public OrderResponse findById(final Long id) {
        final Order order = orderDao.findById(id);
        return OrderResponse.from(order);
    }
}
