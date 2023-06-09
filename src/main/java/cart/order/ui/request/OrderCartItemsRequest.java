package cart.order.ui.request;

import java.util.List;

public class OrderCartItemsRequest {

    private List<OrderCartItemRequest> orderCartItemDtos;

    public OrderCartItemsRequest() {
    }

    public OrderCartItemsRequest(final List<OrderCartItemRequest> orderCartItems) {
        this.orderCartItemDtos = orderCartItems;
    }

    public List<OrderCartItemRequest> getOrderCartItemDtos() {
        return orderCartItemDtos;
    }
}
