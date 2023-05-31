package cart.domain.coupon;

public enum CouponType {
    JOIN_MEMBER_COUPON("신규 가입 축하 쿠폰", 20),
    FIRST_ORDER_COUPON("첫 주문 감사 쿠폰", 10);

    private final String name;
    private final int discountRate;

    CouponType(final String name, final int discountRate) {
        this.name = name;
        this.discountRate = discountRate;
    }

    public String getName() {
        return name;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
