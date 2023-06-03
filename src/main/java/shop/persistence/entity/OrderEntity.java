package shop.persistence.entity;

import java.time.LocalDateTime;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final Long totalPrice;
    private final Long discountedTotalPrice;
    private final Integer deliveryPrice;
    private final LocalDateTime orderAt;

    public OrderEntity(Long id, Long memberId, Long totalPrice, Long discountedTotalPrice,
                       Integer deliveryPrice, LocalDateTime orderAt) {
        this.id = id;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.discountedTotalPrice = discountedTotalPrice;
        this.deliveryPrice = deliveryPrice;
        this.orderAt = orderAt;
    }

    public OrderEntity(Long memberId, Long totalPrice, Long discountedTotalPrice,
                       Integer deliveryPrice, LocalDateTime orderAt) {
        this(null, memberId, totalPrice, discountedTotalPrice, deliveryPrice, orderAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public Long getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }

    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }
}
