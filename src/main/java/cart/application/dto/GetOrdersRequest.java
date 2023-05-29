package cart.application.dto;

import javax.validation.constraints.Positive;

public class GetOrdersRequest {

    @Positive
    private Integer page;

    private GetOrdersRequest() {
    }

    public GetOrdersRequest(Integer page) {
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }
}
