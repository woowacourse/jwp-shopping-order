package cart.step2.order.presentation.dto;

import cart.dto.ProductResponse;
import cart.step2.order.domain.OrderItem;

public class OrderItemResponse {

    private final Long id;
    private final ProductResponse product;
    private final int quantity;

    private OrderItemResponse(final Long id, final ProductResponse productResponse, final int quantity) {
        this.id = id;
        this.product = productResponse;
        this.quantity = quantity;
    }

    public static OrderItemResponse from(final OrderItem orderItem) {
        ProductResponse productResponse = ProductResponse.of(orderItem.getProduct());
        return new OrderItemResponse(orderItem.getId(), productResponse, orderItem.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

}
