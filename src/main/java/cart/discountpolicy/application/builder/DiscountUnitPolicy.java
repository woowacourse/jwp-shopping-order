package cart.discountpolicy.application.builder;

public interface DiscountUnitPolicy {
    int calculateDiscountPrice(int discountValue, int price);
}
