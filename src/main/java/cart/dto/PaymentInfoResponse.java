package cart.dto;

public class PaymentInfoResponse {

    private final Integer totalPrice;
    private final Integer deliveryFee;

    public PaymentInfoResponse(final Integer totalPrice, final Integer deliveryFee) {
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
