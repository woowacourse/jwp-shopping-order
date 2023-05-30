package cart.order.presentation;

public class DeliveyPriceResponse {
    private int price;

    public DeliveyPriceResponse() {
    }

    private DeliveyPriceResponse(int price) {
        this.price = price;
    }

    public static DeliveyPriceResponse from(Integer deliveryPrice) {
        return new DeliveyPriceResponse(deliveryPrice);
    }

    public int getPrice() {
        return price;
    }
}
