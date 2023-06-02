package cart.dto;

import java.util.List;
import java.util.Objects;

public class OrderPageResponse {

    private int totalPages;
    private int currentPage;
    private List<OrderResponse> contents;

    public OrderPageResponse(int totalPages, int currentPage, List<OrderResponse> contents) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.contents = contents;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<OrderResponse> getContents() {
        return contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPageResponse that = (OrderPageResponse) o;
        return totalPages == that.totalPages && currentPage == that.currentPage && Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPages, currentPage, contents);
    }

    @Override
    public String toString() {
        return "OrderPageResponse{" +
                "totalPages=" + totalPages +
                ", currentPage=" + currentPage +
                ", contents=" + contents +
                '}';
    }
}
