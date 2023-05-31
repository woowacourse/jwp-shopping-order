package cart.domain;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import java.math.BigDecimal;
import java.util.List;

public class Order {

    private static final Long DEFAULT_DELIVERY_FEE = 3000L;
    private final Long id;
    private final Long deliveryFee;
    private final MemberCoupon memberCoupon;
    private final Member member;

    private final List<OrderItem> orderItems;

    public Order(final MemberCoupon memberCoupon, final Member member, final List<OrderItem> orderItems) {
        this(null, memberCoupon, member, orderItems);
    }

    public Order(final Long id, final MemberCoupon memberCoupon, final Member member,
                 final List<OrderItem> orderItems) {
        this.id = id;
        this.deliveryFee = DEFAULT_DELIVERY_FEE;
        this.memberCoupon = memberCoupon;
        this.member = member;
        this.orderItems = orderItems;
    }

    public BigDecimal getCalculateDiscountPrice() {
        return memberCoupon.discountPrice(getTotalPrice());
    }

    public Long getTotalPrice() {
        return orderItems.stream()
                .mapToLong(OrderItem::getCalculatePrice)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public MemberCoupon getMemberCoupon() {
        return memberCoupon;
    }

    public Coupon getCoupon() {
        return memberCoupon.getCoupon();
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public BigDecimal getDiscountPrice() {
        return BigDecimal.valueOf(getTotalPrice()).subtract(getCalculateDiscountPrice());
    }
}
