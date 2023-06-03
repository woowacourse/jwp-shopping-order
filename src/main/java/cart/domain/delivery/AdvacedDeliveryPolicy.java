package cart.domain.delivery;

import org.springframework.stereotype.Component;

@Component
public class AdvacedDeliveryPolicy implements DeliveryPolicy {

    private static final long BASIC_FEE = 3000L;
    private static final int FREE_BOUND = 50_000;
    private static final long FREE_FEE = 0L;

    @Override
    public Long getDeliveryFee(final Long productPrice) {
        if (productPrice >= FREE_BOUND) {
            return FREE_FEE;
        }
        return BASIC_FEE;
    }
}
