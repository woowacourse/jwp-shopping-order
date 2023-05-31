package cart.dto;

public class DeliveryPolicyResponse {
    private final int deliveryPrice;
    private final int limit;

    public DeliveryPolicyResponse(int deliveryPrice, int limit) {
        this.deliveryPrice = deliveryPrice;
        this.limit = limit;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public int getLimit() {
        return limit;
    }
}
