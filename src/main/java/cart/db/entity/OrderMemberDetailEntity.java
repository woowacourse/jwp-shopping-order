package cart.db.entity;

import java.time.LocalDateTime;

public class OrderMemberDetailEntity {
    private Long id;
    private final Long memberId;
    private final String name;
    private final String password;
    private final int totalPrice;
    private final int discountedTotalPrice;
    private final int deliveryPrice;
    private final LocalDateTime orderedAt;

    public OrderMemberDetailEntity(
            final Long id,
            final Long memberId,
            final String name,
            final String password,
            final int totalPrice,
            final int discountedTotalPrice,
            final int deliveryPrice,
            final LocalDateTime orderedAt
    ) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.password = password;
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

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
