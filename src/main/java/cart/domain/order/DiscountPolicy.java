package cart.domain.order;

public interface DiscountPolicy {

    Price discount(Price price);
}
