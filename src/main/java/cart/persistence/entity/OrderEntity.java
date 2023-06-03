package cart.persistence.entity;

import cart.domain.Member;
import cart.domain.coupon.MemberCoupon;
import cart.domain.order.Order;
import java.sql.Timestamp;
import java.util.Objects;

public class OrderEntity {

    private final Long id;
    private final long memberId;
    private final Long memberCouponId;
    private final int shippingFee;
    private final int totalPrice;
    private final Timestamp createdAt;

    public OrderEntity(final long memberId, final Long memberCouponId, final int shippingFee, final int totalPrice) {
        this(null, memberId, memberCouponId, shippingFee, totalPrice, null);
    }

    public OrderEntity(final Long id, final long memberId, final Long memberCouponId, final int shippingFee,
            final int totalPrice,
            final Timestamp createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.memberCouponId = memberCouponId;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public static OrderEntity from(final Order order) {
        Member member = order.getMember();
        MemberCoupon memberCoupon = order.getMemberCoupon();
        return new OrderEntity(
                order.getId(),
                member.getId(),
                memberCoupon.getId(),
                order.getShippingFee().getValue(),
                order.getTotalOrderPrice(),
                (Objects.nonNull(order.getCreatedAt()) ? Timestamp.valueOf(order.getCreatedAt()) : null)
        );
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public Long getMemberCouponId() {
        return memberCouponId;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
