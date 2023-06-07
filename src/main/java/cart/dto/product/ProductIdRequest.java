package cart.dto.product;

public class ProductIdRequest {

    private Long id;
    private int quantity;

    private ProductIdRequest() {

    }

    public ProductIdRequest(final Long id, final int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }
}
