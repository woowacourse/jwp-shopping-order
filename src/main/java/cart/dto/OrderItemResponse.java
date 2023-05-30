package cart.dto;

public class OrderItemResponse {
    private final String name;
    private final int quantity;
    private final String imageUrl;
    private final int totalPrice;

    public OrderItemResponse(String name, int quantity, String imageUrl, int totalPrice) {
        this.name = name;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
