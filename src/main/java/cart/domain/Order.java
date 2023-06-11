package cart.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Order {

    private final Long id;
    private final Member owner;
    private final List<OrderItem> orderItems;
    private final LocalDateTime createdAt;

    public Order(Member owner) {
        this(null, owner, Collections.emptyList());
    }

    public Order(Member owner, List<OrderItem> orderItems) {
        this(null, owner, orderItems, LocalDateTime.now());
    }

    public Order(Long id, Member owner, List<OrderItem> orderItems) {
        this(id, owner, orderItems, LocalDateTime.now());
    }

    public Order(Long id, Member owner, List<OrderItem> orderItems, LocalDateTime createdAt) {
        this.id = id;
        this.owner = owner;
        this.orderItems = new ArrayList<>(orderItems);
        this.createdAt = createdAt;
    }

    public static Order of(Member owner, List<CartItem> cartItems) {
        return new Order(null, owner, cartItems.stream()
                .map(OrderItem::new)
                .collect(Collectors.toList()));
    }

    public Order join(Order order) {
        ArrayList<OrderItem> joinedOrderItems = new ArrayList<>(orderItems);
        joinedOrderItems.addAll(order.getOrderItems());
        return new Order(id, owner, joinedOrderItems, createdAt);
    }

    public List<MemberCoupon> getUsedMemberCoupons() {
        return orderItems.stream()
                .flatMap(item -> item.getUsedCoupons().stream())
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Member getOwner() {
        return owner;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
