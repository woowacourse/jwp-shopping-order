package cart.application.service.order.dto;

import java.sql.Timestamp;
import java.util.List;

public class OrderResultDto {

    private final long orderId;
    private final List<OrderedItemResult> orderedItems;
    private final List<UsedCoupon> usedCoupons;
    private final int usedPoint;
    private final int paymentPrice;
    private final Timestamp createdAt;

    public OrderResultDto(Long orderId, List<OrderedItemResult> orderedItems, List<UsedCoupon> usedCoupons, int usedPoint, int paymentPrice, Timestamp createdAt) {
        this.orderId = orderId;
        this.orderedItems = orderedItems;
        this.usedCoupons = usedCoupons;
        this.usedPoint = usedPoint;
        this.paymentPrice = paymentPrice;
        this.createdAt = createdAt;
    }

    public long getOrderId() {
        return orderId;
    }

    public OrderResultDto(Long orderId, List<OrderedItemResult> orderedItems, List<UsedCoupon> usedCoupons, int usedPoint, int paymentPrice) {
        this(orderId, orderedItems, usedCoupons, usedPoint, paymentPrice, null);
    }

    public List<OrderedItemResult> getOrderedItems() {
        return orderedItems;
    }

    public List<UsedCoupon> getUsedCoupons() {
        return usedCoupons;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
