package cart.domain.orderproduct;

public class DeliveryFee {

    private final int deliveryFee;

    public DeliveryFee(final int deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
