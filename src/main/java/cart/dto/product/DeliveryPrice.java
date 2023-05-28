package cart.dto.product;

public class DeliveryPrice {

    private final int deliveryPrice;

    public DeliveryPrice(final int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }
}
