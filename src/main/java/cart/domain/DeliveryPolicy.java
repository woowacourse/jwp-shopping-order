package cart.domain;

public interface DeliveryPolicy {
    Money calculate(Order order);
}
