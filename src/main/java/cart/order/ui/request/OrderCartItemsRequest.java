package cart.order.ui.request;

import java.util.List;

public class OrderCartItemsRequest {

    private List<OrderCartItemRequest> orderCartItems;

    public OrderCartItemsRequest() {
    }

    public OrderCartItemsRequest(final List<OrderCartItemRequest> orderCartItems) {
        this.orderCartItems = orderCartItems;
    }

    public List<OrderCartItemRequest> getOrderCartItems() {
        return orderCartItems;
    }
}
