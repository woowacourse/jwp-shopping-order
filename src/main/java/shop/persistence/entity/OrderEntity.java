package shop.persistence.entity;

import java.time.LocalDateTime;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final Long totalProductPrice;
    private final Integer deliveryPrice;
    private final LocalDateTime orderedAt;

    public OrderEntity(Long id, Long memberId, Long totalProductPrice, Integer deliveryPrice, LocalDateTime orderedAt) {
        this.id = id;
        this.memberId = memberId;
        this.totalProductPrice = totalProductPrice;
        this.deliveryPrice = deliveryPrice;
        this.orderedAt = orderedAt;
    }

    public OrderEntity(Long memberId, Long totalProductPrice, Integer deliveryPrice, LocalDateTime orderedAt) {
        this(null, memberId, totalProductPrice, deliveryPrice, orderedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getTotalProductPrice() {
        return totalProductPrice;
    }

    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
