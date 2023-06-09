package cart.dto.response;

import cart.domain.OrderItem;

public class OrderItemResponse {

    private Long id;
    private int quantity;
    private ProductResponse product;

    private OrderItemResponse() {
    }

    private OrderItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static OrderItemResponse of(OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getId(), orderItem.getQuantity(), ProductResponse.of(orderItem.getProduct()));
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
