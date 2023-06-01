package cart.domain;

public interface DiscountStrategy {
    Payment calculate(Price price);
}
