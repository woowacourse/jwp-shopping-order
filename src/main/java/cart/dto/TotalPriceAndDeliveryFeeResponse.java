package cart.dto;

public class TotalPriceAndDeliveryFeeResponse {
    private final int totalPrice;
    private final int deliveryFee;

    public TotalPriceAndDeliveryFeeResponse(final int totalPrice, final int deliveryFee) {
        this.totalPrice = totalPrice;
        this.deliveryFee = deliveryFee;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
