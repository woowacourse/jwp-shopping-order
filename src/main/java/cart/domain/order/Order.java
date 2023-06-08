package cart.domain.order;

import cart.domain.Member;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final OrderItems orderItems;
    private final OrderPrice orderPrice;
    private final LocalDateTime orderTime;

    private Order(final Long id, final Member member, final OrderItems orderItems, final OrderPrice orderPrice,
        final LocalDateTime orderTime) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.orderPrice = orderPrice;
        this.orderTime = orderTime;
    }

    public static Order notPersisted(final Member member, final OrderItems orderItems,
        final OrderPrice orderPrice, final LocalDateTime orderTime) {
        return new Order(null, member, orderItems, orderPrice, orderTime);
    }

    public static Order persisted(final Long id, final Member member, final OrderItems orderItems,
        final OrderPrice orderPrice, final LocalDateTime orderTime) {
        return new Order(id, member, orderItems, orderPrice, orderTime);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public List<OrderItem> getOrderItems() {
        return orderItems.getItems();
    }

    public Long getProductPrice() {
        return orderItems.getTotalPrice();
    }

    public OrderPrice getOrderPrice() {
        return orderPrice;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }
}
