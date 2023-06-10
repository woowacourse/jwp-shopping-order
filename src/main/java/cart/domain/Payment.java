package cart.domain;

import java.time.LocalDateTime;

public class Payment {
    private final Long id;
    private final Price originalPrice;
    private final Price discountPrice;
    private final Price finalPrice;
    private final LocalDateTime createdAt;

    public Payment(final Long id, final Price originalPrice, final Price discountPrice, final Price finalPrice,
                   final LocalDateTime createdAt) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.finalPrice = finalPrice;
        this.createdAt = createdAt;
    }

    public Payment(final Long id, final Price originalPrice, final Price discountPrice, final Price finalPrice) {
        this(id, originalPrice, discountPrice, finalPrice, null);
    }

    public Payment(final Price originalPrice, final Price discountPrice) {
        this(null, originalPrice, discountPrice, originalPrice.minus(discountPrice), null);
    }

    public Payment(final Price originalPrice) {
        this(null, originalPrice, Price.ZERO_PRICE, originalPrice, null);
    }

    public boolean equalsFinalPrice(Price otherPrice) {
        return otherPrice.equals(this.finalPrice);
    }

    public Price getOriginalPrice() {
        return originalPrice;
    }

    public Price getDiscountPrice() {
        return discountPrice;
    }

    public Price getFinalPrice() {
        return finalPrice;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
