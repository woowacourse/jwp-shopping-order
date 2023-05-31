package cart.domain;

public class DeliveryPolicy {
    private static final int FREE_DELIVERY_STANDARD = 50000;
    private static final int DEFAULT_DELIVERY_FEE = 3000;
    private static final int FREE_DELIVERY_FEE = 0;

    public static int calculateDeliveryFee(int totalProductPrice) {
        if (FREE_DELIVERY_STANDARD < totalProductPrice) {
            return FREE_DELIVERY_FEE;
        }
        return DEFAULT_DELIVERY_FEE;
    }
}
