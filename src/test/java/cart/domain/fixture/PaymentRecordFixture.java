package cart.domain.fixture;

import static cart.domain.fixture.OrderFixture.orderWithoutId;

import cart.domain.order.payment.DeliveryPolicy;
import cart.domain.order.payment.DiscountPolicy;
import cart.domain.order.payment.Payment;
import java.util.List;

public class PaymentRecordFixture {
    public static final Payment PAYMENT = new Payment(orderWithoutId,
            List.of(DiscountPolicy.TEN_PERCENT_DISCOUNT_WHEN_PRICE_IS_UPPER_THAN_FIFTY_THOUSANDS),
            DeliveryPolicy.DEFAULT);

}
