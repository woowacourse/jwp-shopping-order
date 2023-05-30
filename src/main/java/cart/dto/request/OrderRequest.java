package cart.dto.request;

import java.util.List;

public class OrderRequest {

    private Long totalPrice;
    private List<OrderItemDto> order;

    public OrderRequest() {
    }

    public OrderRequest(final Long totalPrice, final List<OrderItemDto> order) {
        this.totalPrice = totalPrice;
        this.order = order;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public List<OrderItemDto> getOrder() {
        return order;
    }
}
