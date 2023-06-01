package cart.domain;

import cart.exception.OrderException;
import java.util.List;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Member member;
    private final Money deliveryFee;
    private final List<OrderItem> orderItems;

    public Order(final Long id, final Member member, final Money deliveryFee, final List<OrderItem> orderItems) {
        this.id = id;
        this.member = member;
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
    }

    public Order(final Member member, final Money deliveryFee, final List<OrderItem> orderItems) {
        this(null, member, deliveryFee, orderItems);
    }

    public Order(final Long id, final Member member, final List<OrderItem> orderItems) {
        this(id, member, null, orderItems);
    }

    public void checkTotalPrice(final Long totalPrice) {
        if (!Objects.equals(totalProductPrice(), totalPrice)) {
            throw new OrderException.OutOfDatedProductPrice();
        }
    }

    public Long totalProductPrice() {
        return orderItems.stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
