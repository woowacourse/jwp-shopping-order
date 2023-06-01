package cart.domain;

import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.exception.CartItemException.CartItemNotExisctException;
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

    public void purchase(final CartItem cartItem) {
        final CartItem findCartItem = get(cartItem.getProduct().getId(), cartItem.getQuantity())
            .orElseThrow(() -> new CartItemNotExisctException("해당 상품이 장바구니에 없습니다."));
        purchaseItems.add(findCartItem);
    }

    private Optional<CartItem> get(final Long productId, final int quantity) {
        return cartItems.stream()
            .filter(cartItem -> cartItem.getProduct().getId().equals(productId) && cartItem.getQuantity() == quantity)
            .findFirst();
    }

    public Order order(final Member member, final LocalDateTime orderTime) {
        if (purchaseItems.isEmpty()) {
            throw new IllegalArgumentException("주문할 상품이 존재하지 않습니다.");
        }
        final List<OrderItem> orderItems = purchaseItems.stream()
            .map(cartItem -> OrderItem.notPersisted(cartItem.getProduct(), cartItem.getQuantity()))
            .collect(Collectors.toList());
        purchaseItems.clear();
        return Order.beforePersisted(member, new OrderItems(orderItems), orderTime);
    }
}
