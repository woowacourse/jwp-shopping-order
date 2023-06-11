package cart.domain;

import cart.exception.PaginationException;

import java.util.Objects;

public class Pagination {

    private static final int PAGE_SIZE = 10;

    private final int totalPage;
    private final int currentPage;
    private final int firstItemIndex;
    private final int pageSize;

    private Pagination(int totalPage, int currentPage, int firstItemIndex, int pageSize) {
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.firstItemIndex = firstItemIndex;
        this.pageSize = pageSize;
    }

    public static Pagination create(int totalItems, int page) {
        validatePage(totalItems, page);
        return new Pagination(calculateTotalPage(totalItems), page,
                calculateFirstIndex(page), PAGE_SIZE);
    }

    private static void validatePage(int totalItems, int page) {
        if (totalItems < (page - 1) * PAGE_SIZE) {
            throw new PaginationException("존재하지 않는 페이지입니다.");
        }
    }

    private static int calculateTotalPage(int totalItems) {
        return (int) Math.ceil((double) totalItems / PAGE_SIZE);
    }

    private static int calculateFirstIndex(int page) {
        return (page - 1) * PAGE_SIZE;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getFirstItemIndex() {
        return firstItemIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagination that = (Pagination) o;
        return totalPage == that.totalPage && currentPage == that.currentPage && firstItemIndex == that.firstItemIndex && pageSize == that.pageSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPage, currentPage, firstItemIndex, pageSize);
    }
}
