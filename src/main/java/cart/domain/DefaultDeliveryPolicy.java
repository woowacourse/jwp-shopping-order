package cart.domain;

public class DefaultDeliveryPolicy implements DeliveryPolicy {

    @Override
    public Money calculateDeliveryFee(Order order) {
        return new Money(3500);
    }
}
