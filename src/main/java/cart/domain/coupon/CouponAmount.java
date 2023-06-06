package cart.domain.coupon;

public class CouponAmount {

    private final int discountAmount;

    private CouponAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public static CouponAmount of(int discountAmount, int minAmount) {
        if (discountAmount < 0) {
            throw new IllegalArgumentException("할인액은 음수가 올 수 없습니다.");
        }
        if (discountAmount > minAmount) {
            throw new IllegalArgumentException("할인액은 최소 주문 금액보다 클 수 없습니다.");
        }
        return new CouponAmount(discountAmount);
    }

    public int getDiscountAmount() {
        return discountAmount;
    }
}
