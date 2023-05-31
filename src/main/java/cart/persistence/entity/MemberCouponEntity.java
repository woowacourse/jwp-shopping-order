package cart.persistence.entity;

public class MemberCouponEntity {
    private final Long id;
    private final Long memberId;
    private final Long couponId;

    public MemberCouponEntity(Long id, Long memberId, Long couponId) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
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
