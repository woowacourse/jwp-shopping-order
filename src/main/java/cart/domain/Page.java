package cart.domain;

import java.util.List;

public class Page<T> {

    private final List<T> contents;
    private final Integer pageSize;
    private final Integer totalPages;
    private final Integer currentPage;

    public Page(List<T> contents, Integer pageSize, Integer totalPages, Integer currentPage) {
        this.contents = contents;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    public List<T> getContents() {
        return contents;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }
}
