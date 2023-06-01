package cart.discountpolicy.application.builder;

public class PercentageDiscountPolicy implements DiscountUnitPolicy {
    @Override
    public int calculateDiscountPrice(int discountValue, int price) {
        return Double.valueOf(price * (discountValue / 100.0)).intValue();
    }
}
