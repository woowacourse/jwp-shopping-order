package cart.dto.purchaseorder;

import java.util.List;

public class PurchaseOrderRequest {

    private Integer usedPoint;
    private List<cart.dto.purchaseorder.PurchaseOrderItemRequest> purchaseOrderItems;

    public PurchaseOrderRequest(Integer usedPoint, List<cart.dto.purchaseorder.PurchaseOrderItemRequest> purchaseOrderItems) {
        this.usedPoint = usedPoint;
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public List<cart.dto.purchaseorder.PurchaseOrderItemRequest> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }
}
