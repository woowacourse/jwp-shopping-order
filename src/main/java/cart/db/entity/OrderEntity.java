package cart.db.entity;

import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final int totalPrice;
    private final int discountedTotalPrice;
    private final int deliveryPrice;
    private final LocalDateTime orderedAt;

    public OrderEntity(
            final Long memberId,
            final int totalPrice,
            final int discountedTotalPrice,
            final int deliveryPrice,
            final LocalDateTime orderedAt
    ) {
        this(null, memberId, totalPrice, discountedTotalPrice, deliveryPrice, orderedAt);
    }

    public OrderEntity(
            final Long id,
            final Long memberId,
            final int totalPrice,
            final int discountedTotalPrice,
            final int deliveryPrice, final LocalDateTime orderedAt
    ) {
        this.id = id;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.discountedTotalPrice = discountedTotalPrice;
        this.deliveryPrice = deliveryPrice;
        this.orderedAt = orderedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", totalPrice=" + totalPrice +
                ", discountedTotalPrice=" + discountedTotalPrice +
                ", deliveryPrice=" + deliveryPrice +
                ", orderedAt=" + orderedAt +
                '}';
    }
}
