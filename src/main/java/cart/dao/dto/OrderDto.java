package cart.dao.dto;

import cart.domain.Coupon;
import cart.domain.Order;
import java.time.LocalDateTime;
import java.util.Optional;

public class OrderDto {

    private static final Long COUPON_NOT_EXISTS = -1L;

    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final LocalDateTime timeStamp;

    public OrderDto(final Long memberId,
                    final Long couponId,
                    final LocalDateTime timeStamp) {
        this(null, memberId, couponId, timeStamp);
    }

    public OrderDto(final Long id,
                    final Long memberId,
                    final Long couponId,
                    final LocalDateTime timeStamp) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.timeStamp = timeStamp;
    }

    public static OrderDto from(final Order order) {
        return new OrderDto(
                order.getMember().getId(),
                getCouponId(order.getOptionalCoupon()),
                order.getTimeStamp()
        );
    }

    private static Long getCouponId(Optional<Coupon> coupon) {
        return coupon.map(Coupon::getId)
                .orElseGet(() -> COUPON_NOT_EXISTS);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Long getCouponId() {
        return couponId;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", couponId=" + couponId +
                ", timeStamp=" + timeStamp +
                '}';
    }

}
