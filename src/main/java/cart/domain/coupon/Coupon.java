package cart.domain.coupon;

public class Coupon {

    private final Long id;
    private final String couponName;
    private final CouponPercent discountPercent;
    private final CouponAmount discountAmount;
    private final MinAmount minAmount;

    public Coupon(Long id, String couponName, Integer discountPercent, int discountAmount, Integer minAmount) {
        validateIsAllZero(discountPercent, discountAmount);
        validateIsAllPositive(discountPercent, discountAmount);
        this.id = id;
        this.couponName = couponName;
        this.discountPercent = CouponPercent.from(discountPercent);
        this.discountAmount = CouponAmount.of(discountAmount, minAmount);
        this.minAmount = MinAmount.from(minAmount);
    }

    private void validateIsAllPositive(Integer discountPercent, int discountAmount) {
        if (discountAmount != 0 && discountPercent != 0) {
            throw new IllegalArgumentException("쿠폰은 정량, 정액 할인 하나만 가능합니다.");
        }
    }

    private void validateIsAllZero(Integer discountPercent, int discountAmount) {
        if (discountAmount == 0 && discountPercent == 0) {
            throw new IllegalArgumentException("쿠폰은 정량, 정액 할인 하나만 가능합니다.");
        }
    }

    public Coupon(String couponName, int discountPercent, int discountAmount, int minAmount) {
        this(null, couponName, discountPercent, discountAmount, minAmount);
    }

    public Long getId() {
        return id;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getDiscountPercent() {
        return discountPercent.getDiscountPercent();
    }

    public int getDiscountAmount() {
        return discountAmount.getDiscountAmount();
    }

    public Integer getMinAmount() {
        return minAmount.getMinAmount();
    }

}
