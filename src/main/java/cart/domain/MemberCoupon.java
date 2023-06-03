package cart.domain;

public class MemberCoupon {

    private Long id;
    private Long memberId;
    private Coupon coupon;
    private Boolean used;

    public MemberCoupon(Long memberId, Coupon coupon) {
        this(null, memberId, coupon, false);
    }

    public MemberCoupon(Long id, Long memberId, Coupon coupon, Boolean used) {
        this.id = id;
        this.memberId = memberId;
        this.coupon = coupon;
        this.used = used;
    }

    public int apply(int paymentAmount) {
        return coupon.apply(paymentAmount);
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

    public Boolean isUsed() {
        return used;
    }
}
