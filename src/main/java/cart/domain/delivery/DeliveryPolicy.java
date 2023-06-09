package cart.domain.delivery;

public interface DeliveryPolicy {

    Long getDeliveryFee(final Long productPrice);
}
