package cart.dto;

import java.util.List;
import java.util.Objects;

public class PurchaseOrderResponse {

    private int totalPages;
    private int currentPage;
    private int pageSize;
    private List<PurchaseOrderInfoResponse> purchaseOrderInfoRespons;

    public PurchaseOrderResponse(int totalPages, int currentPage, int pageSize,
                                 List<PurchaseOrderInfoResponse> purchaseOrderInfoRespons) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.purchaseOrderInfoRespons = purchaseOrderInfoRespons;
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

    public List<PurchaseOrderInfoResponse> getPurchaseOrderItemsResponses() {
        return purchaseOrderInfoRespons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrderResponse that = (PurchaseOrderResponse) o;
        return totalPages == that.totalPages && currentPage == that.currentPage && pageSize == that.pageSize && Objects.equals(purchaseOrderInfoRespons, that.purchaseOrderInfoRespons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPages, currentPage, pageSize, purchaseOrderInfoRespons);
    }

    @Override
    public String toString() {
        return "PurchaseOrderResponse{" +
                "totalPages=" + totalPages +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", purchaseOrderInfoRespons=" + purchaseOrderInfoRespons +
                '}';
    }
}
