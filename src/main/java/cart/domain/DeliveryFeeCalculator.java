package cart.domain;

public interface DeliveryFeeCalculator {
    Money calculate(Order order);
}
