package cart.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class OrderHistory {

    private Long id;
    private int originalPrice;
    private int usedPoint;
    private int orderPrice;
    private final Member member;
    private List<OrderItem> orderItems = new LinkedList<>();


    public OrderHistory(final int originalPrice, final int usedPoint, final int orderPrice, final Member member) {
        this(null, originalPrice, usedPoint, orderPrice, member);
    }

    public OrderHistory(final Long id, final int originalPrice, final int usedPoint, final int orderPrice,
                        final Member member) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.orderPrice = orderPrice;
        this.member = member;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public Long getId() {
        return id;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderHistory that = (OrderHistory) o;
        return originalPrice == that.originalPrice && usedPoint == that.usedPoint && orderPrice == that.orderPrice
                && Objects.equals(id, that.id) && Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, originalPrice, usedPoint, orderPrice, member);
    }
}
