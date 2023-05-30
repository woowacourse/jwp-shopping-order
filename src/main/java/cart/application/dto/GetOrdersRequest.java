package cart.application.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class GetOrdersRequest {

    @NotNull(message = "양수의 페이지가 입력되어야 합니다.")
    @Positive(message = "양수의 페이지가 입력되어야 합니다.")
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
