package cart.dao.dto;

public class OrderProductDto {

    private final long orderId;
    private final long productId;
    private final String productName;
    private final int price;
    private final String imageUrl;

    public OrderProductDto(final long orderId, final long productId, final String productName, final int price, final String imageUrl) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
