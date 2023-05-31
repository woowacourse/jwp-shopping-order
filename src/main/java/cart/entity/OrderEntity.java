package cart.entity;

import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final int totalPrice;
    private final int discountedTotalPrice;
    private final int deliveryFee;
    private final LocalDateTime orderedAt;
    private final Long memberId;

    public OrderEntity(
            final Long id,
            final int totalPrice,
            final int discountedTotalPrice,
            final int deliveryFee,
            final LocalDateTime orderedAt,
            final Long memberId
    ) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.discountedTotalPrice = discountedTotalPrice;
        this.deliveryFee = deliveryFee;
        this.orderedAt = orderedAt;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public Long getMemberId() {
        return memberId;
    }
}
