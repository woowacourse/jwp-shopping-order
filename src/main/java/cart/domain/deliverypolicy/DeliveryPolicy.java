package cart.domain.deliverypolicy;

import cart.domain.Money;

public interface DeliveryPolicy {

    Money getDeliveryFee(Money totalOrderPrice);
}
