package cart.application.response;

import java.util.List;

public class OrdersResponse {

    private List<OrderWithOutTotalPriceResponse> orders;

    public OrdersResponse(List<OrderWithOutTotalPriceResponse> orders) {
        this.orders = orders;
    }

    public static OrdersResponse from(List<OrderWithOutTotalPriceResponse> orderResponses) {
        return new OrdersResponse(orderResponses);
    }

    public List<OrderWithOutTotalPriceResponse> getOrders() {
        return orders;
    }
}
