package cart.domain.discount;

public interface DiscountPolicy {

    Long calculate(final Long price);
}
