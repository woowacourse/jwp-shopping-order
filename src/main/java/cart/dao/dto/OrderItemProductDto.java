package cart.dao.dto;

public class OrderItemProductDto {

    private final long orderItemId;
    private final long productId;
    private final int quantity;
    private final String productName;
    private final int price;
    private final String productImageUrl;

    public OrderItemProductDto(long orderItemId, long productId, int quantity, String productName,
        int price, String productImageUrl) {
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.price = price;
        this.productImageUrl = productImageUrl;
    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }
}
