package cart.dto.order;

public class OrderProductResponse {

    private final long productId;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int quantity;
    private final int totalPrice;

    public OrderProductResponse(long productId, String name, int price, String imageUrl, int quantity, int totalPrice) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public long getProductId() {
        return productId;
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

    public int getTotalPrice() {
        return totalPrice;
    }
}
