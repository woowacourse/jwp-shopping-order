package cart.domain.order;

import cart.domain.cart.CartItems;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.OrderItemEntity;
import java.util.ArrayList;
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

    public static OrderItems of(final List<OrderItemEntity> orderItemEntities, List<Product> products, Member member) {
        validateSameSize(orderItemEntities, products);
        List<OrderItem> items = new ArrayList<>();
        for (int index = 0; index < orderItemEntities.size(); index++) {
            final OrderItem orderItem = OrderItem.of(orderItemEntities.get(index), products.get(index), member);
            items.add(orderItem);
        }
        return new OrderItems(items, member);
    }

    private static void validateSameSize(final List<OrderItemEntity> orderItemEntities, final List<Product> products) {
        if (orderItemEntities.size() != products.size()) {
            throw new IllegalArgumentException("주문에 포함된 상품 수와 조회된 상품 수가 일치하지 않습니다");
        }
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
