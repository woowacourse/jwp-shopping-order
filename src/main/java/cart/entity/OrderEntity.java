package cart.entity;

import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final int totalItemPrice;
    private final int discountedTotalItemPrice;
    private final int shippingFee;
    private final LocalDateTime orderedAt;

    public OrderEntity(
            final Long id,
            final int totalItemPrice,
            final int discountedTotalItemPrice,
            final int shippingFee,
            final LocalDateTime orderedAt
    ) {
        this.id = id;
        this.totalItemPrice = totalItemPrice;
        this.discountedTotalItemPrice = discountedTotalItemPrice;
        this.shippingFee = shippingFee;
        this.orderedAt = orderedAt;
    }

    public Long getId() {
        return id;
    }

    public int getTotalItemPrice() {
        return totalItemPrice;
    }

    public int getDiscountedTotalItemPrice() {
        return discountedTotalItemPrice;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
