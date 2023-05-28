package cart.domain.cart;

public class DeliveryFee {

    private static final int DEFAULT_DELIVERY_FEE = 3000;

    private int fee;

    private DeliveryFee(final int fee) {
        this.fee = fee;
    }

    public static DeliveryFee createDefault() {
        return new DeliveryFee(DEFAULT_DELIVERY_FEE);
    }

    public static DeliveryFee from(final int fee) {
        return new DeliveryFee(fee);
    }

    public void setFee(final int fee) {
        this.fee = fee;
    }

    public int getFee() {
        return fee;
    }
}
