package cart.ui.order;

import cart.ui.order.dto.CreateOrderItemRequest;

public class CreateOrderItemDto {
    private final Long cartItemId;
    private final Long productId;
    private final Integer quantity;

    public CreateOrderItemDto(final Long cartItemId, final Long productId, final Integer quantity) {
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
