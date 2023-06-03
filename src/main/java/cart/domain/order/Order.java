package cart.domain.order;

import cart.domain.Member;

import cart.domain.coupon.Coupon;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final Long id;
    private final Member member;
    private final List<OrderItem> orderItems;
    private final List<Coupon> coupons;
    private final int paymentPrice;
    private final int totalPrice;
    private final int point;
    private final String createdAt;

    public Order(final Long id,
                 final Member member,
                 final List<OrderItem> orderItems,
                 final List<Coupon> coupons,
                 final int paymentPrice,
                 final int totalPrice,
                 final int point,
                 final String createdAt) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.coupons = coupons;
        this.paymentPrice = paymentPrice;
        this.totalPrice = totalPrice;
        this.point = point;
        this.createdAt = createdAt;

    }

    public Order(final int paymentPrice,
                 final int totalPrice,
                 final int point,
                 final Member member,
                 final List<OrderItem> orderItems,
                 final List<Coupon> coupons,
                 final String createdAt) {
        this(null, member, orderItems, coupons, paymentPrice, totalPrice, point, createdAt);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPoint() {
        return point;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
