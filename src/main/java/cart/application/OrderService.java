package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.CartItemException;
import cart.exception.OrderException;
import cart.exception.OrderException.OutOfDatedProductPrice;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO repository 분리
@Service
@Transactional
public class OrderService {

    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderService(final CartItemDao cartItemDao,
                        final OrderDao orderDao,
                        final OrderItemDao orderItemDao) {
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public Long add(final Member member, final OrderRequest orderRequest) {
        final Long orderId = orderDao.save(new OrderEntity(member.getId(), orderRequest.getDeliveryFee()));

        final List<Long> cartItemIds = orderRequest.getCartItemIds();
        final List<CartItem> cartItems = cartItemIds.stream()
                .map(id -> findCartItem(member, id))
                .collect(Collectors.toList());

        validateTotalPrice(orderRequest.getTotalPrice(), cartItems);
        orderItemDao.saveAll(OrderItemEntity.of(orderId, cartItems));
        cartItemDao.deleteByIds(cartItemIds);
        return orderId;
    }

    private CartItem findCartItem(final Member member, final Long cartItemId) {
        final CartItem cartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new CartItemException.IllegalId(cartItemId));
        cartItem.checkOwner(member);
        return cartItem;
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
        final List<OrderEntity> orderEntities = orderDao.findByMemberId(member.getId());
        final List<Order> orders = new ArrayList<>();
        for (final OrderEntity order : orderEntities) {
            final List<OrderItemEntity> orderItems = orderItemDao.findByOrderId(order.getId());
            orders.add(new Order(order.getId(), OrderItem.from(orderItems)));
        }
        return OrderResponse.from(orders);
    }

    public OrderDetailResponse findOrderDetailById(final Member member, final Long orderId) {
        final OrderEntity order = orderDao.find(member.getId(), orderId)
                .orElseThrow(() -> new OrderException.IllegalId(orderId));
        final List<OrderItemEntity> orderItems = orderItemDao.findByOrderId(orderId);

        return OrderDetailResponse.from(
                new Order(order.getId(), OrderItem.from(orderItems), new Money(order.getDeliveryFee())));
    }

    public void remove(final Member member, final Long orderId) {
        final Long memberId = member.getId();
        // TODO isExist 사용?
        final OrderEntity order = orderDao.find(memberId, orderId)
                .orElseThrow(() -> new OrderException.IllegalId(orderId));
        orderDao.deleteById(orderId);
        orderItemDao.deleteByOrderId(orderId);
    }
}
