package cart.domain.order;

import cart.domain.CartItems;
import cart.domain.Member;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItems {

    private final List<OrderItem> orderItems;
    private final Member member;

    private OrderItems(final List<OrderItem> orderItems, final Member member) {
        validateItemsLength(orderItems);
        this.orderItems = orderItems;
        this.member = member;
    }

    private void validateItemsLength(final List<OrderItem> orderItems) {
        if (orderItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public static OrderItems from(final CartItems cartItems) {
        final List<OrderItem> orderItems = cartItems.getCartItems().stream()
                .map(cartItem -> OrderItem.from(cartItem))
                .collect(Collectors.toUnmodifiableList());

        return new OrderItems(orderItems, cartItems.getMember());
    }

    public Price sumOfPrice() {
        int sum = orderItems.stream()
                .mapToInt(OrderItem::getPrice)
                .sum();
        return new Price(sum);
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Member getMember() {
        return member;
    }
}
