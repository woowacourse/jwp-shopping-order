package cart.application.dto;

import java.util.List;

public class GetOrdersResponse {

    private final int totalPages;
    private final int currentPage;
    private final int pageSize;
    private final List<OrderContents> contents;

    public GetOrdersResponse(int totalPages, int currentPage, int pageSize, List<OrderContents> contents) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.contents = contents;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<OrderContents> getContents() {
        return contents;
    }
}
