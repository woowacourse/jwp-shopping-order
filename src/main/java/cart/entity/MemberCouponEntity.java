package cart.entity;

public class MemberCouponEntity {

    private Long id;
    private Long memberId;
    private Long couponId;
    private Boolean used;

    public MemberCouponEntity(Long memberId, Long couponId, Boolean used) {
        this(null, memberId, couponId, used);
    }

    public MemberCouponEntity(Long id, Long memberId, Long couponId, Boolean used) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.used = used;
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

    public boolean isUsed() {
        return used;
    }
}
