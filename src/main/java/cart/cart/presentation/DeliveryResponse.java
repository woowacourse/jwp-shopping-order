package cart.cart.presentation;

public class DeliveryResponse {
    private long price;
    private long limit;

    public DeliveryResponse(long price, long limit) {
        this.price = price;
        this.limit = limit;
    }

    public long getPrice() {
        return price;
    }

    public long getLimit() {
        return limit;
    }
}
