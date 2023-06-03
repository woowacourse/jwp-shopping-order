package cart.domain.fixture;

import cart.domain.DefaultDeliveryPolicy;
import cart.domain.Money;
import cart.domain.PaymentRecord;

import java.util.Map;

import static cart.domain.fixture.DiscountPolicyFixture.defaultDiscountPolicy;
import static cart.domain.fixture.OrderFixture.orderWithoutId;

public class PaymentRecordFixture {

    public static final PaymentRecord paymentRecord = new PaymentRecord(orderWithoutId,
        Money.from(55_000),
        Map.of(defaultDiscountPolicy, Money.from(5_500)),
        Map.of(new DefaultDeliveryPolicy("기본 배송 정책", Money.from(3_500)), Money.from(3_500)));

}
