package cart.dto;

public class PageInfo {

    private int page;
    private int size;
    private int totalElements;
    private int totalPages;

    public PageInfo() {
    }

    public PageInfo(final int page, final int size, final int totalElements, final int totalPages) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
