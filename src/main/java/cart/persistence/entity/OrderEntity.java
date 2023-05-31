package cart.persistence.entity;

import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final Integer totalPrice;
    private final Integer discountedTotalPrice;
    private final Integer deliveryPrice;
    private final LocalDateTime orderedAt;

    public OrderEntity(final Long memberId, final Integer totalPrice, final Integer discountedTotalPrice,
                       final Integer deliveryPrice, final LocalDateTime orderedAt) {
        this(null, memberId, totalPrice, discountedTotalPrice, deliveryPrice, orderedAt);
    }

    public OrderEntity(final Long id, final Long memberId, final Integer totalPrice, final Integer discountedTotalPrice,
                       final Integer deliveryPrice, final LocalDateTime orderedAt) {
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

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }

    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
