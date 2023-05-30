package cart.dto;

import javax.validation.constraints.NotNull;

public class HomePagingRequest {

    @NotNull(message = "현재 페이지에 있는 마지막 상품의 ID를 입력하세요")
    private final Long lastId;

    @NotNull(message = "보고 싶은 상품의 개수를 입력하세요")
    private final Integer pageItemCount;

    public HomePagingRequest(final Long lastId, final Integer pageItemCount) {
        this.lastId = lastId;
        this.pageItemCount = pageItemCount;
    }

    public Long getLastId() {
        return lastId;
    }

    public Integer getPageItemCount() {
        return pageItemCount;
    }
}
