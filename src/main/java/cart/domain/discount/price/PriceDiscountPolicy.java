package cart.domain.discount.price;

public interface PriceDiscountPolicy {
    Integer calculateDiscountPrice(Integer price);
}
