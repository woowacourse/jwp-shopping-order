package cart.dto.purchaseorder.response;

import cart.dto.purchaseorder.PurchaseOrderItemInfoResponse;

import java.util.List;
import java.util.Objects;

public class PurchaseOrderPageResponse {

    private int totalPages;
    private int currentPage;
    private int pageSize;
    private List<PurchaseOrderItemInfoResponse> contents;

    public PurchaseOrderPageResponse(int totalPages, int currentPage, int pageSize,
                                     List<PurchaseOrderItemInfoResponse> contents) {
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

    public List<PurchaseOrderItemInfoResponse> getContents() {
        return contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrderPageResponse that = (PurchaseOrderPageResponse) o;
        return totalPages == that.totalPages && currentPage == that.currentPage && pageSize == that.pageSize && Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPages, currentPage, pageSize, contents);
    }

    @Override
    public String toString() {
        return "PurchaseOrderResponse{" +
                "totalPages=" + totalPages +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", purchaseOrderInfoRespons=" + contents +
                '}';
    }
}
