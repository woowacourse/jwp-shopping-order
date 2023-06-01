package cart.dao.dto;

public class MemberCouponDto {

    private final Long id;
    private final Long memberId;
    private final Long couponId;

    public MemberCouponDto(final Long id, final Long memberId, final Long couponId) {
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
