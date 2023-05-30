package cart.order;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private final Long id;
    private final List<OrderItem> orderItems;
    private final List<OrderCoupon> orderCoupons;
    private final Integer deliveryPrice;
    private final Timestamp orderedTime;
    private final Long memberId;

    public Order(Long id, List<OrderItem> orderItems, List<OrderCoupon> orderCoupons, Integer deliveryPrice, Timestamp orderedTime, Long memberId) {
        this.id = id;
        this.orderItems = orderItems;
        this.orderCoupons = orderCoupons;
        this.deliveryPrice = deliveryPrice;
        this.orderedTime = orderedTime;
        this.memberId = memberId;
    }

    public Order(List<OrderItem> orderItems, List<OrderCoupon> orderCoupons, Integer deliveryPrice, Timestamp orderedTime, Long memberId) {
        this.id = null;
        this.orderItems = orderItems;
        this.orderCoupons = orderCoupons;
        this.deliveryPrice = deliveryPrice;
        this.orderedTime = orderedTime;
        this.memberId = memberId;
    }

    public Order assignId(Long id) {
        return new Order(id, orderItems, orderCoupons, deliveryPrice, orderedTime, memberId);
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public List<OrderCoupon> getOrderCoupons() {
        return orderCoupons;
    }

    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    public Timestamp getOrderedTime() {
        return orderedTime;
    }

    public Long getMemberId() {
        return memberId;
    }
}
