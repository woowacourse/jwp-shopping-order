package cart.entity;

public class MemberCouponEntity {

    private Long id;
    private Long memberId;
    private Long couponId;

    public MemberCouponEntity(Long memberId, Long couponId) {
        this(null, memberId, couponId);
    }

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
