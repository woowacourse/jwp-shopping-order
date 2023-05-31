package cart.domain.coupon;

public enum CouponType {
    WELCOME_JOIN("회원가입 감사 쿠폰", 5);

    private final String name;
    private final Integer discountRate;

    CouponType(String name, Integer discountRate) {
        this.name = name;
        this.discountRate = discountRate;
    }

    public String getName() {
        return name;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }
}
