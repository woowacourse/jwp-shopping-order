package cart.repository.dto;

import cart.entity.OrderEntity;

import java.time.LocalDateTime;

public class OrderInfoDto {

    private final long id;
    private final int originalPrice;
    private final int discountPrice;
    private final LocalDateTime createdAt;

    private OrderInfoDto(final long id, final int originalPrice, final int discountPrice, final LocalDateTime createdAt) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.createdAt = createdAt;
    }

    public static OrderInfoDto from(final OrderEntity entity) {
        return new OrderInfoDto(entity.getId(), entity.getOriginalPrice(), entity.getDiscountPrice(), entity.getCreatedAt());
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
}
