package cart.ui.dto.order;

public class CartItemsPriceResponse {

    private final Integer totalPrice;
    private final Integer deliveryFee;

    public CartItemsPriceResponse(final Integer totalPrice, final Integer deliveryFee) {
        this.totalPrice = totalPrice;
        this.deliveryFee = deliveryFee;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }
}
