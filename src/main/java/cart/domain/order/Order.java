package cart.domain.order;


import cart.domain.Member;
import cart.domain.point.Point;
import cart.domain.shipping.ShippingFee;
import cart.exception.order.OrderException;

import java.util.List;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Member member;
    private final ShippingFee shippingFee;
    private final Long totalPrice;
    private final List<OrderItem> orderItems;
    private final Point usedPoint;
    private final String createdAt;

    public Order(Member member, ShippingFee shippingFee, Long totalPrice, List<OrderItem> orderItems, Point usedPoint) {
        this(null, member, shippingFee, totalPrice, orderItems, usedPoint, null);
    }

    public Order(Long id, Member member, ShippingFee shippingFee, Long totalPrice, List<OrderItem> orderItems, Point usedPoint, String createdAt) {
        this.id = id;
        this.member = member;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    public static Order of(Member member, ShippingFee shippingFee, List<OrderItem> orderItems, Long threshold, Point usedPoint) {
        Long getTotalPrice = orderItems.stream()
                .mapToLong(orderItem -> orderItem.getProduct().getPrice() * orderItem.getQuantity())
                .sum();
        if (getTotalPrice >= threshold) {
            return new Order(member, new ShippingFee(0L), getTotalPrice, orderItems, usedPoint);
        }

        return new Order(member, shippingFee, getTotalPrice, orderItems, usedPoint);
    }

    public void checkSameTotalPrice(Long requestTotalProductsPrice) {
        if (!Objects.equals(this.totalPrice, requestTotalProductsPrice)) {
            throw new OrderException.NotSameTotalPrice();
        }
    }

    public void checkMinusOrderPrice(Long requestUsedPoint) {
        if (this.totalPrice + this.shippingFee.getFee() < requestUsedPoint) {
            throw new OrderException.MinusOrderPrice();
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public ShippingFee getShippingFee() {
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
