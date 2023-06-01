package cart.cart.domain.deliveryprice;

public class DeliveryPrice {
    public static final int DEFAULT_PRICE = 3_000;
    public static final int FREE_LIMIT = 30_000;
    private final int price;

    public DeliveryPrice() {
        this.price = DEFAULT_PRICE;
    }

    public DeliveryPrice(int price) {
        this.price = price;
    }

    public static DeliveryPrice calculateDeliveryPrice(int totalPrice) {
        if (FREE_LIMIT < totalPrice) {
            return new DeliveryPrice(0);
        }
        return new DeliveryPrice();
    }

    public int getPrice() {
        return price;
    }
}
