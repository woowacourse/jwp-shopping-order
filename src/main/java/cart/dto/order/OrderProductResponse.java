package cart.dto.order;

public class OrderProductResponse {

    private final long productId;
    private final String name;
    private final String imageUrl;
    private final int quantity;
    private final int price;
    private final int totalPrice;

    public OrderProductResponse(long productId, String name, int price, String imageUrl, int quantity, int totalPrice) {
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public long getProductId() {
        return productId;
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

    public int getTotalPrice() {
        return totalPrice;
    }
}
