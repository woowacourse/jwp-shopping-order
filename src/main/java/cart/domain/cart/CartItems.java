package cart.domain.cart;

import cart.domain.member.Member;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.order.Quantity;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public static CartItems from(final List<CartItem> cartItems) {
        return new CartItems(cartItems);
    }

    public OrderItems mapToOrderItems() {
        final List<OrderItem> orderItems = cartItems.stream()
                .map(it -> new OrderItem(it.getProduct(), Quantity.from(it.getQuantity())))
                .collect(toList());

        return new OrderItems(orderItems);
    }

    public boolean isNotSameSize(final int size) {
        return cartItems.size() != size;
    }

    public Member getMember() {
        return cartItems.get(0).getMember();
    }

    public Long getMemberId() {
        return cartItems.get(0).getMemberId();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
