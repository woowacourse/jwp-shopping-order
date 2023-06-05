package cart.domain;

import java.util.List;

public class Order {
    private final Member member;
    private final List<OrderItem> orderItems;
    private final MemberCoupon memberCoupon;
    private Long id;

    public Order(Member member, List<OrderItem> orderItems, MemberCoupon memberCoupon) {
        this.member = member;
        this.orderItems = orderItems;
        this.memberCoupon = memberCoupon;
    }

    public Order(Long id, Member member, List<OrderItem> orderItems, MemberCoupon memberCoupon) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.memberCoupon = memberCoupon;
    }

    public Money getTotalPrice() {
        return new Money(orderItems.stream().mapToInt(OrderItem::getPrice).sum());
    }

    public Money getDiscountPrice() {
        Money totalPrice = getTotalPrice();
        return Money.subtract(totalPrice, getDiscountedPrice());
    }

    public Money getShippingFee() {
        return ShippingFeePolicy.findShippingFee(getTotalPrice());
    }

    public Money getDiscountedPrice() {
        return memberCoupon.getCoupon().discount(getTotalPrice());
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

    public MemberCoupon getMemberCoupon() {
        return memberCoupon;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }
}
