package cart.dto;

import java.util.List;

public class PagingOrderResponse {

    private PageInfo pageInfo;
    private List<OrderResponse> orders;

    public PagingOrderResponse() {
    }

    public PagingOrderResponse(final PageInfo pageInfo, final List<OrderResponse> orders) {
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
