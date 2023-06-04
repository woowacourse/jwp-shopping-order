package cart.dto;

public class PageInfo {
    private Integer page;
    private Integer size;
    private Integer totalElements;
    private Integer totalPages;

    public PageInfo() {
    }

    public PageInfo(final Integer page, final Integer size, final Integer totalElements, final Integer totalPages) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }
}
