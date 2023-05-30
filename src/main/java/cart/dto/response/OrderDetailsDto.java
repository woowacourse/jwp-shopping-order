package cart.dto.response;

public class OrderDetailsDto {

    private final long quantity;
    private final ProductResponse product;

    public OrderDetailsDto(final long quantity, final ProductResponse product) {
        this.quantity = quantity;
        this.product = product;
    }

    public long getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
