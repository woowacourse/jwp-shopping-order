package cart.dto;

import java.util.List;

public class OrderResponse {
    private Long orderId;
    private List<OrderedProduct> orderedProducts;

    public OrderResponse() {
    }

    public OrderResponse(final Long orderId, final List<OrderedProduct> orderedProducts) {
        this.orderId = orderId;
        this.orderedProducts = orderedProducts;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }
}
