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

    public static Order of(final Long id, final List<OrderItem> orderItems, final Member member, final Coupon coupon) {
        Integer price = orderItems.stream()
                .mapToInt(OrderItem::calculatePrice)
                .sum();
        ShippingFee shippingFee = ShippingFee.from(price);
        return new Order(id, shippingFee, orderItems, coupon, member);
    }

    public Integer calculateTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::calculatePrice)
                .sum();
    }

    public Integer calculateDiscountPrice() {
        Integer totalPrice = calculateTotalPrice();
        return coupon.calculateDiscount(totalPrice);
    }

    // TODO: 6/1/23 실제 주문하는 로직이 필요할것 같긴 함
    public void order() {

    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalArgumentException("다른 회원의 주문입니다.");
        }
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
