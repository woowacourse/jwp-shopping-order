package cart.domain.discount;

public class PolicyDiscount implements Policy {

    private int value;

    public PolicyDiscount(final int value) {
        this.value = value;
    }

    @Override
    public int calculate(final int price) {
        if (price < value) {
            return 0;
        }

        return price - value;
    }

    @Override
    public void updateDiscountValue(final int value) {
        this.value = value;
    }
}
