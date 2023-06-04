package cart.application.service.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResultDto {

    private final long orderId;
    private final List<OrderedItemResult> orderedItems;
    private final List<UsedCoupon> usedCoupons;
    private final int usedPoint;
    private final int paymentPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    public OrderResultDto(Long orderId, List<OrderedItemResult> orderedItems, List<UsedCoupon> usedCoupons, int usedPoint, int paymentPrice, LocalDateTime createdAt) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
