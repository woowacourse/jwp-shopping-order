package cart.entity;

public class MemberCouponEntity {
    private final Long id;
    private final Long couponId;
    private final Long memberId;
    private final Boolean available;

    public MemberCouponEntity(Long couponId, Long memberId, Boolean available) {
        this(null,couponId,memberId,available);
    }

    public MemberCouponEntity(Long id, Long couponId, Long memberId, Boolean available) {
        this.id = id;
        this.couponId = couponId;
        this.memberId = memberId;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Boolean getAvailable() {
        return available;
    }
}
