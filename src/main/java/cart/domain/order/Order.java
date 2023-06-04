package cart.domain.order;


import cart.domain.Member;
import cart.domain.point.Point;

import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final Long shippingFee;
    private final Long totalPrice;
    private final List<OrderItem> orderItems;
    private final Point usedPoint;
    private final String createdAt;

    public Order(Member member, Long shippingFee, Long totalPrice, List<OrderItem> orderItems, Point usedPoint) {
        this(null, member, shippingFee, totalPrice, orderItems, usedPoint, null);
    }

    public Order(Long id, Member member, Long shippingFee, Long totalPrice, List<OrderItem> orderItems, Point usedPoint, String createdAt) {
        this.id = id;
        this.member = member;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    public static Order of(Member member, Long shippingFee, List<OrderItem> orderItems, Long threshold,Point usedPoint) {
        Long getTotalPrice = orderItems.stream()
                .mapToLong(orderItem -> orderItem.getProduct().getPrice() * orderItem.getQuantity())
                .sum();
        if (getTotalPrice >= threshold) {
            return new Order(member, 0L, getTotalPrice, orderItems, usedPoint);
        }

        return new Order(member, shippingFee, getTotalPrice, orderItems, usedPoint);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getShippingFee() {
        return shippingFee;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Long getUsedPoint() {
        return usedPoint.getPoint();
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
