package cart.domain.fixture;

import static cart.domain.fixture.DiscountPolicyFixture.defaultDiscountPolicy;
import static cart.domain.fixture.OrderFixture.orderWithoutId;

import cart.domain.DefaultDeliveryPolicy;
import cart.domain.Money;
import cart.domain.PaymentRecord;
import java.util.Map;

public class PaymentRecordFixture {
    public static final PaymentRecord paymentRecord = new PaymentRecord(orderWithoutId, Money.from(55_000),
            Map.of(defaultDiscountPolicy, Money.from(5_500)),
            Map.of(new DefaultDeliveryPolicy(), Money.from(3_500)));

}
