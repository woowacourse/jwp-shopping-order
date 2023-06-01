package cart.step2.order.domain;

import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final int price;
    private final Long couponId;
    private final Long memberId;
    private final LocalDateTime date;

    private OrderEntity(final Long id, final int price, final Long couponId, final Long memberId, final LocalDateTime date) {
        this.id = id;
        this.price = price;
        this.couponId = couponId;
        this.memberId = memberId;
        this.date = date;
    }

    public static OrderEntity createNonePkOrder(final int price, final Long couponId, final Long memberId) {
        return new OrderEntity(null, price, couponId, memberId, null);
    }

    public static OrderEntity of(final Long id, final int price, final Long couponId, final Long memberId, final LocalDateTime date) {
        return new OrderEntity(id, price, couponId, memberId, date);
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getDate() {
        return date;
    }

}
