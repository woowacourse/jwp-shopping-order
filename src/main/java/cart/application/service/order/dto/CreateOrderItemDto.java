package cart.application.service.order.dto;

import cart.ui.order.dto.request.CreateOrderItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderItemDto {
    private final Long cartItemId;
    private final Long productId;
    private final Integer quantity;

    public CreateOrderItemDto(final Long cartItemId, final Long productId, final Integer quantity) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<CreateOrderItemDto> from(final List<CreateOrderItemRequest> createOrderItemRequests) {
        return createOrderItemRequests.stream()
                .map(createOrderItemRequest -> new CreateOrderItemDto(
                        createOrderItemRequest.getCartItemId(),
                        createOrderItemRequest.getProductId(),
                        createOrderItemRequest.getQuantity())
                ).collect(Collectors.toUnmodifiableList());

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
