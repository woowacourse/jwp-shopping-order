package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.CartItemException.IllegalId;
import cart.exception.OrderException;
import cart.exception.OrderException.OutOfDatedProductPrice;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderDao orderDao;
    private final CartItemDao cartItemDao;

    public OrderService(final OrderDao orderDao, final CartItemDao cartItemDao) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
    }

    public Long add(final Member member, final OrderRequest orderRequest) {
        final Long orderId = orderDao.save(member.getId(), new Money(orderRequest.getDeliveryFee()));
        final List<OrderItem> orderItems = createOrderItems(orderId, member, orderRequest);

        orderDao.saveOrderItems(orderItems);
        cartItemDao.deleteByIds(orderRequest.getCartItemIds());
        return orderId;
    }

    private List<OrderItem> createOrderItems(final Long orderId, final Member member, final OrderRequest orderRequest) {
        final List<Long> cartItemIds = orderRequest.getCartItemIds();
        final List<CartItem> cartItems = cartItemDao.findByIds(cartItemIds);
        validateIds(member, cartItemIds, cartItems);
        validateTotalPrice(orderRequest.getTotalPrice(), cartItems);
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

    private void validateTotalPrice(final Long totalPrice, final List<CartItem> cartItems) {
        final long totalPriceFromQuery = cartItems.stream()
                .mapToLong(item -> item.getProduct().getPrice().getValue() * item.getQuantity())
                .sum();
        if (totalPriceFromQuery != totalPrice) {
            throw new OutOfDatedProductPrice();
        }
    }

    public List<OrderResponse> findOrdersByMember(final Member member) {
        final List<Order> orders = orderDao.findByMemberId(member.getId());
        return OrderResponse.from(orders);
    }

    public OrderDetailResponse findOrderDetailById(final Member member, final Long orderId) {
        final Order order = orderDao.findDetailById(member.getId(), orderId)
                .orElseThrow(() -> new OrderException.IllegalId(orderId));
        final List<OrderItem> orderItems = orderDao.findOrderItemsById(member.getId(), orderId);
        return OrderDetailResponse.from(new Order(order.getId(), orderItems, order.getDeliveryFee()));
    }
}
