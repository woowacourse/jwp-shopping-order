package cart.domain.discount;

public class PolicyDiscount implements Policy {

    private static final int FREE = 0;

    private int value;

    public PolicyDiscount(final int value) {
        this.value = value;
    }

    @Override
    public int calculate(final int price) {
        if (price < value) {
            return FREE;
        }

        return price - value;
    }

    @Override
    public int updateDiscountValue(final int value) {
        this.value = value;
        return value;
    }
}
