package cart.ui.order.dto.request;

public class CreateOrderItemRequest {

    private Long cartItemId;
    private Long productId;
    private Integer quantity;

    public CreateOrderItemRequest() {
    }

    public CreateOrderItemRequest(Long cartItemId, Long productId, Integer quantity) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
