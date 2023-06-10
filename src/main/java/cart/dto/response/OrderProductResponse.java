package cart.dto.response;

public class OrderProductResponse {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int quantity;

    public OrderProductResponse(final Long id, final String name, final int price, final String imageUrl,
        final int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
