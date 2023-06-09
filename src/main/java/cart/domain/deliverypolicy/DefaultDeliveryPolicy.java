package cart.domain.deliverypolicy;

import cart.domain.Money;
import org.springframework.stereotype.Component;

@Component
public class DefaultDeliveryPolicy implements DeliveryPolicy {

    public static final int DEFAULT_DELIVER_FEE_AMOUNT = 3000;

    @Override
    public Money getDeliveryFee(final Money totalOrderPrice) {
        return new Money(DEFAULT_DELIVER_FEE_AMOUNT);
    }
}
