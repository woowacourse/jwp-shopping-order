package cart.dto;

public class OrderProductResponse {
    private final ProductResponse product;
    private final int quantity;

    public OrderProductResponse(final ProductResponse product, final int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
