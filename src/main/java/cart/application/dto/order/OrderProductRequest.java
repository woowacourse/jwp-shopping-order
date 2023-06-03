package cart.application.dto.order;

public class OrderProductRequest {

    private long cartItemId;
    private int quantity;
    private String name;
    private int price;
    private String imageUrl;

    public OrderProductRequest() {
    }

    public OrderProductRequest(final long cartItemId, final int quantity, final String name, final int price,
            final String imageUrl) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
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
}
