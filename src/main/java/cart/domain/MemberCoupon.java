package cart.domain;

public class MemberCoupon {

    private Long id;
    private Long memberId;
    private Coupon coupon;

    public MemberCoupon(Long memberId, Coupon coupon) {
        this(null, memberId, coupon);
    }

    public MemberCoupon(Long id, Long memberId, Coupon coupon) {
        this.id = id;
        this.memberId = memberId;
        this.coupon = coupon;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}
