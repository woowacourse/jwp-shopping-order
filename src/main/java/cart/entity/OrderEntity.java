package cart.entity;

import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final long memberId;
    private final int originalPrice;
    private final int discountPrice;
    private final LocalDateTime createdAt;

    public OrderEntity(final Long id, final long memberId, final int originalPrice,
                       final int discountPrice, final LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.createdAt = createdAt;
    }

    public OrderEntity(final long memberId, final int originalPrice, final int discountPrice) {
        this(null, memberId, originalPrice, discountPrice, null);
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
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
