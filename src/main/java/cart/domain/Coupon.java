package cart.domain;

public interface Coupon {
    Coupon EMPTY_COUPON = new Coupon() {
        @Override
        public boolean isAvailable(final Integer totalPrice) {
            throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
        }

        @Override
        public Integer calculateDiscount(final Integer totalPrice) {
            throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
        }

        @Override
        public CouponInfo getCouponInfo() {
            throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
        }
    };

    boolean isAvailable(final Integer totalPrice);

    Integer calculateDiscount(final Integer totalPrice);

    CouponInfo getCouponInfo();
}
