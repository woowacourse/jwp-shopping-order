package cart.dto.response;

public class OrderProductInfo {
    private long id;
    private int quantity;
    public ProductResponse product;

    public OrderProductInfo() {
    }

    public OrderProductInfo(final long id, final int quantity, final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
