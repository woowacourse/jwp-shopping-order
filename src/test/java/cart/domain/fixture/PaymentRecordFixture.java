package cart.domain.fixture;

import static cart.domain.fixture.OrderFixture.orderWithoutId;

import cart.domain.DeliveryPolicies;
import cart.domain.DiscountPolicies;
import cart.domain.PaymentRecord;
import java.util.List;

public class PaymentRecordFixture {
    public static final PaymentRecord paymentRecord = new PaymentRecord(orderWithoutId,
            List.of(DiscountPolicies.TEN_PERCENT_DISCOUNT_WHEN_PRICE_IS_UPPER_THAN_FIFTY_THOUSANDS),
            DeliveryPolicies.DEFAULT);

}
