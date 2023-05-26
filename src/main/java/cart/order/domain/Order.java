package cart.order.domain;

import static cart.order.exception.OrderExceptionType.NON_EXIST_ORDER_ITEM;

import cart.order.exception.OrderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Order {

    private final Long id;
    private final Long memberId;
    private final List<OrderItem> orderItems = new ArrayList<>();

    public Order(Long memberId, OrderItem... orderItems) {
        this(null, memberId, Arrays.asList(orderItems));
    }

    public Order(Long memberId, List<OrderItem> orderItems) {
        this(null, memberId, orderItems);
    }

    public Order(Long id, Long memberId, OrderItem... orderItems) {
        this(id, memberId, Arrays.asList(orderItems));
    }

    public Order(Long id, Long memberId, List<OrderItem> orderItems) {
        validate(orderItems);
        this.id = id;
        this.memberId = memberId;
        this.orderItems.addAll(orderItems);
    }

    private void validate(List<OrderItem> orderItems) {
        if (orderItems.isEmpty()) {
            throw new OrderException(NON_EXIST_ORDER_ITEM);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    public int totalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::price)
                .sum();
    }
}
