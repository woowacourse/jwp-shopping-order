package cart.domain;

import java.util.List;
import java.util.Objects;

public class Order {
    private final Long id;
    private final ShippingFee shippingFee;
    private final List<OrderItem> orderItems;
    private final Coupon coupon;
    private final Member member;

    public Order(final Long id, final ShippingFee shippingFee, final List<OrderItem> orderItems, final Coupon coupon, final Member member) {
        this.id = id;
        this.shippingFee = shippingFee;
        this.orderItems = orderItems;
        this.coupon = coupon;
        this.member = member;
    }

    public static Order of(final List<OrderItem> orderItems, final Member member, final Coupon coupon) {
        Integer price = orderItems.stream()
                .mapToInt(OrderItem::calculatePrice)
                .sum();
        ShippingFee shippingFee = ShippingFee.from(price);
        return new Order(null, shippingFee, orderItems, coupon, member);
    }

    public Integer calculatePaymentPrice() {
        Integer price = orderItems.stream()
                .mapToInt(OrderItem::calculatePrice)
                .sum();
        if (coupon.isAvailable(price)) {
            throw new IllegalArgumentException("사용할 수 없는 쿠폰입니다");
        }
        return price - coupon.calculateDiscount(price) + shippingFee.getCharge();
    }

    public Long getId() {
        return id;
    }

    public ShippingFee getShippingFee() {
        return shippingFee;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", shippingFee=" + shippingFee +
                ", orderItems=" + orderItems +
                ", coupon=" + coupon +
                ", member=" + member +
                '}';
    }
}
