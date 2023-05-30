package cart.order;

public class OrderItem {
    private final Long productId;
    private final String productName;
    private final Integer price;
    private final Integer quantity;
    private final String imgUri;

    public OrderItem(Long productId, String productName, Integer price, Integer quantity, String imgUri) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imgUri = imgUri;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getImgUri() {
        return imgUri;
    }
}
