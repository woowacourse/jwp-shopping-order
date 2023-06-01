package cart.dto;

import java.util.List;

public class OrderCartItemsRequest {

    private List<OrderCartItemDto> orderCartItems;

    public OrderCartItemsRequest() {
    }

    public OrderCartItemsRequest(final List<OrderCartItemDto> orderCartItems) {
        this.orderCartItems = orderCartItems;
    }

    public List<OrderCartItemDto> getOrderCartItems() {
        return orderCartItems;
    }
}
