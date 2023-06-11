package cart.dto.purchaseorder.request;

public class PurchaseOrderItemRequest {

    private Long productId;
    private Integer quantity;

    public PurchaseOrderItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "PurchaseOrderItemRequest{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
