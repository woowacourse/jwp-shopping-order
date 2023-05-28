package cart.dto.product;

import cart.domain.cart.DeliveryFee;

public class DeliveryFeeResponse {

    private final int price;

    private DeliveryFeeResponse(final int deliveryFee) {
        this.price = deliveryFee;
    }

    public static DeliveryFeeResponse from(final int deliveryFee) {
        return new DeliveryFeeResponse(deliveryFee);
    }

    public static DeliveryFeeResponse from(final DeliveryFee deliveryFee) {
        return new DeliveryFeeResponse(deliveryFee.getFee());
    }

    public int getDeliveryPrice() {
        return price;
    }
}
