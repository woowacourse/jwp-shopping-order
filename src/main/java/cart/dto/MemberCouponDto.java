package cart.dto;

public class MemberCouponDto {

    private final Long id;
    private final Long memberId;
    private final Long couponId;

    public MemberCouponDto(final Long id, final Long memberId, final Long couponId) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
    }

    public MemberCouponDto(final Long memberId, final Long couponId) {
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

    @Override
    public String toString() {
        return "MemberCouponDto{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", couponId=" + couponId +
                '}';
    }

}
