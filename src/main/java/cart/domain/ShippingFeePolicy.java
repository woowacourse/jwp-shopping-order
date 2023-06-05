package cart.domain;

public class ShippingFeePolicy {
    private static final int IMPOSING_STANDARD = 30000;
    private static final int SHIPPING_FEE = 3000;

    public static Money findShippingFee(Money totalPrice) {
        if (totalPrice.value() < IMPOSING_STANDARD) {
            return new Money(SHIPPING_FEE);
        }
        return new Money(0);
    }
}
