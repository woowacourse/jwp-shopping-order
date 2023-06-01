package cart.dao.entity;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final Integer shippingFee;
    private final Integer totalPrice;
    private final LocalDateTime createdAt;

    public OrderEntity(final Long id, final Long memberId, final Long couponId, final Integer shippingFee, final Integer totalPrice, final LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public static OrderEntity from(final Order order) {
        return new OrderEntity(
                null,
                order.getMember().getId(),
                order.getCoupon().getCouponInfo().getId(),
                order.getShippingFee().getCharge(),
                order.calculatePaymentPrice(),
                null
        );
    }

    public Order toOrder(final Member member, final Map<Long, List<OrderItem>> orderItemByOrderId, final Map<Long, Coupon> couponById) {

        return Order.of(
                orderItemByOrderId.get(id),
                member,
                couponById.getOrDefault(couponId, Coupon.EMPTY_COUPON)
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getCouponId() {
        return couponId;
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
                && Objects.equals(couponId, that.couponId)
                && Objects.equals(shippingFee, that.shippingFee)
                && Objects.equals(totalPrice, that.totalPrice)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, couponId, shippingFee, totalPrice, createdAt);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", couponId=" + couponId +
                ", shippingFee=" + shippingFee +
                ", totalPrice=" + totalPrice +
                ", createdAt=" + createdAt +
                '}';
    }
}
