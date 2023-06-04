package cart.dto.product;

public class ProductIdResponse {

    private Long id;

    private ProductIdResponse() {
    }

    public ProductIdResponse(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
