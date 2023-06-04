package cart.dto.purchaseorder.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class PurchaseOrderResponse {

    private Long orderId;
    private LocalDateTime orderAt;
    private int payAmount;
    private int usedPoint;
    private int savedPoint;
    private List<PurchaseOrderItemResponse> products;

    public PurchaseOrderResponse(Long orderId, LocalDateTime orderAt, int payAmount, int usedPoint,
                                 int savedPoint, List<PurchaseOrderItemResponse> products) {
        this.orderId = orderId;
        this.orderAt = orderAt;
        this.payAmount = payAmount;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.products = products;
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getSavedPoint() {
        return savedPoint;
    }

    public List<PurchaseOrderItemResponse> getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrderResponse that = (PurchaseOrderResponse) o;
        return payAmount == that.payAmount && usedPoint == that.usedPoint && savedPoint == that.savedPoint && Objects.equals(orderId, that.orderId) && Objects.equals(orderAt, that.orderAt) && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderAt, payAmount, usedPoint, savedPoint, products);
    }

    @Override
    public String toString() {
        return "PurchaseOrderResponse{" +
                "orderId=" + orderId +
                ", orderAt=" + orderAt +
                ", payAmount=" + payAmount +
                ", usedPoint=" + usedPoint +
                ", savedPoint=" + savedPoint +
                ", products=" + products +
                '}';
    }
}
