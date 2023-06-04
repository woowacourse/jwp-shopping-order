package cart.dao.entity;

import cart.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final Long memberCouponId;
    private final Integer shippingFee;
    private final Integer totalPrice;
    private final LocalDateTime createdAt;

    public OrderEntity(final Long id, final Long memberId, final Long memberCouponId, final Integer shippingFee, final Integer totalPrice, final LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.memberCouponId = memberCouponId;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public static OrderEntity from(final Order order) {
        return new OrderEntity(
                null,
                order.getMember().getId(),
                order.getMemberCoupon().getId(),
                order.getShippingFee().getCharge(),
                order.calculateTotalPrice(),
                null
        );
    }

    public Order toOrder(final Member member, final Map<Long, List<OrderItem>> orderItemByOrderId, final Map<Long, MemberCoupon> memberCoupons) {
        return new Order(
                id,
                ShippingFee.findByCharge(shippingFee),
                orderItemByOrderId.get(id),
                memberCoupons.getOrDefault(memberCouponId, new EmptyMemberCoupon()),
                member
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getMemberCouponId() {
        return memberCouponId;
    }

    public Integer getShippingFee() {
        return shippingFee;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderEntity that = (OrderEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(memberId, that.memberId)
                && Objects.equals(memberCouponId, that.memberCouponId)
                && Objects.equals(shippingFee, that.shippingFee)
                && Objects.equals(totalPrice, that.totalPrice)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, memberCouponId, shippingFee, totalPrice, createdAt);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", couponId=" + memberCouponId +
                ", shippingFee=" + shippingFee +
                ", totalPrice=" + totalPrice +
                ", createdAt=" + createdAt +
                '}';
    }
}
