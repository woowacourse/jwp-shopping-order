package cart.dto.request;

public class CartItemRequest {
    private Long cartItemId;
    private int quantity;
    private String name;
    private int price;
    private String imageUrl;

    public CartItemRequest() {
    }

    public CartItemRequest(Long cartItemId, int quantity, String name, int price, String imageUrl) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getCartItemId() {
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
