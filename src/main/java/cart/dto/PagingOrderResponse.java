package cart.dto;

import java.util.List;

public class PagingOrderResponse {

    private PageInfo pageInfo;
    private List<OrderResponse> orderResponses;

    public PagingOrderResponse() {
    }

    public PagingOrderResponse(final PageInfo pageInfo, final List<OrderResponse> orderResponses) {
        this.pageInfo = pageInfo;
        this.orderResponses = orderResponses;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }
}
