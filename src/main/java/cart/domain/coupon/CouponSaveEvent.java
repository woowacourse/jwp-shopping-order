package cart.domain.coupon;

public class CouponSaveEvent {

    private final Long memberId;

    public CouponSaveEvent(final Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
