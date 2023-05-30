package cart.domain;

import cart.domain.vo.Amount;

public interface Coupon {

    Amount calculateProduct(Amount productAmount);
    Amount calculateDelivery(Amount deliveryAmount);
}
