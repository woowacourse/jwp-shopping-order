package cart.dto;

import java.util.List;

public class PurchaseOrderRequest {

    private Integer usedPoint;
    private List<PurchaseOrderItemRequest> purchaseOrderItems;

    public PurchaseOrderRequest(Integer usedPoint, List<PurchaseOrderItemRequest> purchaseOrderItems) {
        this.usedPoint = usedPoint;
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public List<PurchaseOrderItemRequest> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }
}
