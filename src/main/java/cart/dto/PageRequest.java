package cart.dto;

public class PageRequest {

    private Integer page;
    private Integer size;

    public PageRequest() {
    }

    public PageRequest(final Integer page, final Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }
}
