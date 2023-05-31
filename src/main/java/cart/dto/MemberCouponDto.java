package cart.dto;

public class MemberCouponDto {

    // 여기서는 Coupon 의 member_id, 그리고 Coupon_id 가 필요하다.
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

    @Override
    public String toString() {
        return "MemberCouponDto{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", couponId=" + couponId +
                '}';
    }

}
