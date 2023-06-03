package cart.ui.order.dto;

public class CreateOrderItemDto {
    private final Long cartItemId;
    private final Long productId;
    private final Integer quantity;

    public CreateOrderItemDto(Long cartItemId, Long productId, Integer quantity) {
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

    public static CreateOrderItemDto from(final CreateOrderItemRequest createOrderItemRequest) {
        return new CreateOrderItemDto(createOrderItemRequest.getCartItemId(), createOrderItemRequest.getProductId(), createOrderItemRequest.getQuantity());

    }
}
