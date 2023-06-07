package cart.paging;

import cart.dto.PageInfo;
import cart.dto.PageRequest;

public class Paging {

    private static final int ORDER_COUNT = 1;

    private final int page;
    private final int size;

    public Paging(final PageRequest pageRequest) {
        this.page = pageRequest.getPage();
        this.size = pageRequest.getSize();
    }

    public int getStart() {
        return (page - ORDER_COUNT) * size;
    }

    public int getSize() {
        return size;
    }

    public PageInfo getPageInfo(final int totalElement) {
        final int totalPage = (int) Math.ceil(totalElement / (double) size);
        return new PageInfo(page, size, totalElement, totalPage);
    }
}
