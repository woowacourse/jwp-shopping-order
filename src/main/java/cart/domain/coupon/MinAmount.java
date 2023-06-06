package cart.domain.coupon;

public class MinAmount {
    private final int minAmount;

    private MinAmount(int minAmount) {
        if (minAmount < 0) {
            throw new IllegalArgumentException("최소 주문 금액은 음수가 될 수 없습니다.");
        }
        this.minAmount = minAmount;
    }

    public static MinAmount from(int minAmount) {
        return new MinAmount(minAmount);
    }

    public int getMinAmount() {
        return minAmount;
    }
}
