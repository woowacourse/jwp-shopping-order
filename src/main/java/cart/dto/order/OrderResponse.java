package cart.dto.order;

import java.util.List;

public class OrderResponse {
    private final Long id;
    private final List<OrderProductResponse> orderProducts;
    private final Boolean confirmState;

    public OrderResponse(Long id, List<OrderProductResponse> orderProducts, Boolean confirmState) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.confirmState = confirmState;
    }

    public Long getId() {
        return id;
    }

    public List<OrderProductResponse> getOrderProducts() {
        return orderProducts;
    }

    public Boolean getConfirmState() {
        return confirmState;
    }
}
