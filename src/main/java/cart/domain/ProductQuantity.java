package cart.domain;

public class ProductQuantity {
    private final Long productId;
    private final int price;
    private final int quantity;

    public ProductQuantity(Long productId, int price, int quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
