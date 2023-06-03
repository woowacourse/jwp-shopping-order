package cart.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final BigDecimal totalPrice;
    private final BigDecimal discountedTotalPrice;
    private final Integer deliveryPrice;
    private final LocalDateTime orderedAt;

    public OrderEntity(final Long memberId, final BigDecimal totalPrice, final BigDecimal discountedTotalPrice,
                       final Integer deliveryPrice, final LocalDateTime orderedAt) {
        this(null, memberId, totalPrice, discountedTotalPrice, deliveryPrice, orderedAt);
    }

    public OrderEntity(final Long id, final Long memberId, final BigDecimal totalPrice,
                       final BigDecimal discountedTotalPrice,
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }

    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
