package cart.dto;

import java.util.List;

public class OrderResponses {
    private PageInfo pageInfo;
    private List<OrderResponse> orders;

    public OrderResponses() {
    }

    public OrderResponses(final PageInfo pageInfo, final List<OrderResponse> orders) {
        this.pageInfo = pageInfo;
        this.orders = orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}

