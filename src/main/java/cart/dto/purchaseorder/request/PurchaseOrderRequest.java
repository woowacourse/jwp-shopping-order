package cart.dto.purchaseorder.request;

import java.util.List;

public class PurchaseOrderRequest {

    private Integer usedPoint;
    private List<PurchaseOrderItemRequest> products;

    public PurchaseOrderRequest(Integer usedPoint, List<PurchaseOrderItemRequest> products) {
        this.usedPoint = usedPoint;
        this.products = products;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public List<PurchaseOrderItemRequest> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "PurchaseOrderRequest{" +
                "usedPoint=" + usedPoint +
                ", purchaseOrderItems=" + products +
                '}';
    }
}
