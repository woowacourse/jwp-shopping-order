package cart.domain.coupon;

public class CouponPercent {

    private final int discountPercent;

    private CouponPercent(int discountPercent) {
        validate(discountPercent);
        this.discountPercent = discountPercent;
    }

    private static void validate(int percent) {
        if (percent > 100 || percent < 0) {
            throw new IllegalArgumentException("할일률은 음수 혹은 100을 넘길 수 없습니다.");
        }
    }

    public static CouponPercent from(int percent) {
        return new CouponPercent(percent);
    }

    public int getDiscountPercent() {
        return discountPercent;
    }
}
