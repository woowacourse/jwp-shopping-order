package cart.application.service.coupon.dto;

public class MemberCouponDto {

    private final long id;
    private final long memberId;
    private final long couponId;

    public MemberCouponDto(long id, long memberId, long couponId) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public long getCouponId() {
        return couponId;
    }

}
