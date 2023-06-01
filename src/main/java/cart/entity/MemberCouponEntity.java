package cart.entity;

public class MemberCouponEntity {
    private final Long id;
    private final Long couponId;
    private final Long memberId;
    private final Boolean availability;

    public static final MemberCouponEntity EMPTY = new MemberCouponEntity(
            null,
            null, null, true
    );

    public MemberCouponEntity(Long couponId, Long memberId, Boolean availability) {
        this(null,couponId,memberId,availability);
    }

    public MemberCouponEntity(Long id, Long couponId, Long memberId, Boolean availability) {
        this.id = id;
        this.couponId = couponId;
        this.memberId = memberId;
        this.availability = availability;
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

    public Boolean getAvailability() {
        return availability;
    }
}
