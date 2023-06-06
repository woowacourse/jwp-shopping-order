package cart.domain;

import cart.entity.OrderEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderInfo {

    private final long id;
    private final int originalPrice;
    private final int discountPrice;
    private final LocalDateTime createdAt;

    public OrderInfo(final long id, final int originalPrice, final int discountPrice, final LocalDateTime createdAt) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.createdAt = createdAt;
    }

    public static OrderInfo from(final OrderEntity entity) {
        return new OrderInfo(entity.getId(), entity.getOriginalPrice(), entity.getDiscountPrice(), entity.getCreatedAt());
    }

    public boolean isIdEqual(final long id) {
        return this.id == id;
    }

    public int getPaymentAmount() {
        return originalPrice - discountPrice;
    }

    public long getId() {
        return id;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderInfo)) return false;
        OrderInfo orderInfo = (OrderInfo) o;
        return id == orderInfo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
