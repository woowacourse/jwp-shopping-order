package cart.dto.product;

public class DeliveryPriceResponse {

    private final int deliveryPrice;

    public DeliveryPriceResponse(final int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }
}
