package cart.dto.page;

import cart.dao.dto.PageInfo;

public class PageResponse {
    private final int total;
    private final int perPage;
    private final int currentPage;
    private final int lastPage;

    private PageResponse(final int total, final int perPage, final int currentPage, final int lastPage) {
        this.total = total;
        this.perPage = perPage;
        this.currentPage = currentPage;
        this.lastPage = lastPage;
    }

    public static PageResponse from(PageInfo pageInfo) {
        return new PageResponse(pageInfo.getTotal(),
                pageInfo.getPerPage(),
                pageInfo.getCurrentPage(),
                pageInfo.getLastPage());
    }

    public int getTotal() {
        return total;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }
}
