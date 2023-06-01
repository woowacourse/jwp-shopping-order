package cart.discountpolicy.application.builder;

public class AbsoluteDiscountPolicy implements DiscountUnitPolicy {
    @Override
    public int calculateDiscountPrice(int discountValue, int price) {
        return discountValue;
    }
}
