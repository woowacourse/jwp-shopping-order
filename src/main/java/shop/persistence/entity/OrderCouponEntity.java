package shop.persistence.entity;

public class OrderCouponEntity {
    private final Long id;
    private final Long memberId;
    private final Long couponId;

    public OrderCouponEntity(Long id, Long memberId, Long couponId) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
    }

    public OrderCouponEntity(Long memberId, Long couponId) {
        this(null, memberId, couponId);
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
}
