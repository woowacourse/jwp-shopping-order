package cart.dao.dto;

import cart.domain.coupon.Coupon;
import cart.domain.order.Order;
import java.time.LocalDateTime;
import java.util.Optional;

public class OrderDto {

    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final LocalDateTime timeStamp;

    public OrderDto(final Long id, final Long memberId, final Long couponId, final LocalDateTime timeStamp) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.timeStamp = timeStamp;
    }

    public OrderDto(final Long memberId, final Long couponId, final LocalDateTime timeStamp) {
        this(null, memberId, couponId, timeStamp);
    }

    public static OrderDto from(final Order order) {
        final Optional<Coupon> coupon = order.getCoupon();
        return coupon.map(value -> new OrderDto(order.getMemberId(), value.getId(), order.getTimeStamp()))
                .orElseGet(() -> new OrderDto(order.getMemberId(), null, order.getTimeStamp()));
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

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
