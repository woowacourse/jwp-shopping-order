package cart.domain;

import cart.domain.delivery.DeliveryPolicy;
import cart.domain.discount.DiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.order.OrderPrice;
import cart.exception.CartItemException.CartItemNotExistException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CartItems {

    private final List<CartItem> cartItems;
    private final Set<CartItem> purchaseItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.purchaseItems = new HashSet<>();
    }

    public void buy(final Long productId, final int quantity) {
        final CartItem findCartItem = findByProductIdAndQuantity(productId, quantity)
            .orElseThrow(() -> new CartItemNotExistException("해당 상품이 장바구니에 없습니다."));
        purchaseItems.add(findCartItem);
    }

    private Optional<CartItem> findByProductIdAndQuantity(final Long productId, final int quantity) {
        return cartItems.stream()
            .filter(cartItem -> cartItem.getProduct().getId().equals(productId) && cartItem.getQuantity() == quantity)
            .findFirst();
    }

    public Order order(final Member member, final LocalDateTime orderTime, final DiscountPolicy discountPolicy,
        final DeliveryPolicy deliveryPolicy) {
        if (purchaseItems.isEmpty()) {
            throw new IllegalArgumentException("주문할 상품이 존재하지 않습니다.");
        }
        final List<OrderItem> orderItemList = purchaseItems.stream()
            .map(cartItem -> OrderItem.notPersisted(cartItem.getProduct(), cartItem.getQuantity()))
            .collect(Collectors.toList());

        purchaseItems.clear();

        final OrderItems orderItems = new OrderItems(orderItemList);
        return Order.notPersisted(member, orderItems,
            OrderPrice.of(orderItems.getTotalPrice(), discountPolicy, deliveryPolicy), orderTime);
    }
}
