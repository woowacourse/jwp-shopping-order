package cart.ui.dto.response;

public class OrderProductResponse {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private int quantity;

    public OrderProductResponse() {
    }

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
