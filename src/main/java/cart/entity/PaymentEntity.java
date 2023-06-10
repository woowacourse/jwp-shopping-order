package cart.entity;

import java.time.LocalDateTime;

public class PaymentEntity {
    private final Long id;
    private final Long memberId;
    private final Integer originalPrice;
    private final Integer discountPrice;
    private final Integer finalPrice;
    private final LocalDateTime createdAt;

    public PaymentEntity(final Long id, final Long memberId, final Integer originalPrice, final Integer discountPrice,
                         final Integer finalPrice,
                         final LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.finalPrice = finalPrice;
        this.createdAt = createdAt;
    }

    public PaymentEntity(final Long memberId, final Integer originalPrice, final Integer discountPrice,
                         final Integer finalPrice,
                         final LocalDateTime createdAt) {
        this(null, memberId, originalPrice, discountPrice, finalPrice, createdAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
