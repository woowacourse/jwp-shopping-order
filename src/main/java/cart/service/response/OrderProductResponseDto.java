package cart.service.response;

public class OrderProductResponseDto {

    private final ProductResponse productResponse;
    private final Integer quantity;

    private OrderProductResponseDto() {
        this(null, null);
    }

    public OrderProductResponseDto(final ProductResponse productResponse, final Integer quantity) {
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
