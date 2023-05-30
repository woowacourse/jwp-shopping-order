package cart.dto;

public class HomePagingRequest {

    private final Long lastId;
    private final int pageItemCount;

    public HomePagingRequest(final Long lastId, final int pageItemCount) {
        this.lastId = lastId;
        this.pageItemCount = pageItemCount;
    }

    public Long getLastId() {
        return lastId;
    }

    public int getPageItemCount() {
        return pageItemCount;
    }
}
