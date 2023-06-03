package cart.domain.delivery;

public class BasicDeliveryPolicy implements DeliveryPolicy {

    private static final long BASIC_FEE = 3000L;

    @Override
    public Long getDeliveryFee(final Long productPrice) {
        return BASIC_FEE;
    }
}
