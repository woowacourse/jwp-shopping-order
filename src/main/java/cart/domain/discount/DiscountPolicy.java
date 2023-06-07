package cart.domain.discount;

public interface DiscountPolicy {
    int calculateDiscountAmount(int price);

    default DiscountPolicy and(DiscountPolicy discountPolicy) {
        return (price) -> discountPolicy.calculateDiscountAmount(price) + calculateDiscountAmount(price);
    }
}
