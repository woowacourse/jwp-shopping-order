package cart.fixture;

import cart.domain.Payment;
import cart.domain.Price;

public class PaymentFixture {
    public static final Payment PAYMENT_1 = new Payment(1L, new Price(10_000), new Price(2_000), new Price(8_000));
}
