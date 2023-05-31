package cart.domain.couponissuer;

public enum CouponId {
    NO_COUPON(0L),
    OVER_FIFTY_PRICE_COUPON(1L);

    private final long id;

    CouponId(long id) {
        this.id = id;
    }
}
