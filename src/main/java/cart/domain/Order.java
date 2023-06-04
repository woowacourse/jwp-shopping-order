package cart.domain;

import cart.domain.vo.Amount;
import java.util.List;

public class Order {

    private final Long id;
    private final List<OrderItem> orderItems;
    private final MemberCoupon coupon;
    private final Amount totalProductAmount;
    private final Amount deliveryAmount;
    private final String address;

    public Order(final List<OrderItem> orderItems, final MemberCoupon coupon, final Amount totalProductAmount, final Amount deliveryAmount, final String address) {
        this(null, orderItems, coupon, totalProductAmount, deliveryAmount, address);
    }

    public Order(final Long id, final List<OrderItem> orderItems, final MemberCoupon coupon, final Amount totalProductAmount,
                 final Amount deliveryAmount,
                 final String address) {
        this.id = id;
        this.orderItems = orderItems;
        this.coupon = coupon;
        this.totalProductAmount = totalProductAmount;
        this.deliveryAmount = deliveryAmount;
        this.address = address;
    }

    public Amount discountProductAmount() {
        return coupon.calculateProduct(totalProductAmount);
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public MemberCoupon getCoupon() {
        return coupon;
    }

    public Amount getTotalProductAmount() {
        return totalProductAmount;
    }

    public Amount getDeliveryAmount() {
        return deliveryAmount;
    }

    public String getAddress() {
        return address;
    }
}
