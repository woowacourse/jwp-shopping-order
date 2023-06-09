package cart.dto.response;

public class OrderProductResponse {

    private final String name;
    private final String imageUrl;
    private final int quantity;
    private final int price;

    public OrderProductResponse(
            final String name,
            final String imageUrl,
            final int quantity,
            final int price
    ) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
