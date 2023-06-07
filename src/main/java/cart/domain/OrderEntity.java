package cart.domain;

import java.util.Objects;

public class OrderEntity {

    private final Long id;
    private final String createdAt;
    private final Long memberId;
    private final int totalProductPrice;
    private final int totalDeliveryFee;
    private final int usePoint;
    private final int totalPrice;

    public OrderEntity(
            final Long id,
            final String createdAt,
            final Long memberId,
            final int totalProductPrice,
            final int totalDeliveryFee,
            final int usePoint,
            final int totalPrice
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.memberId = memberId;
        this.totalProductPrice = totalProductPrice;
        this.totalDeliveryFee = totalDeliveryFee;
        this.usePoint = usePoint;
        this.totalPrice = totalPrice;
    }

    public OrderEntity(
            final String createdAt,
            final Long memberId,
            final int totalProductPrice,
            final int totalDeliveryFee,
            final int usePoint,
            final int totalPrice
    ) {
        this(null, createdAt, memberId, totalProductPrice, totalDeliveryFee, usePoint, totalPrice);
    }

    public Long getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getTotalProductPrice() {
        return totalProductPrice;
    }

    public int getTotalDeliveryFee() {
        return totalDeliveryFee;
    }

    public int getUsePoint() {
        return usePoint;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity orderEntity = (OrderEntity) o;
        return Objects.equals(id, orderEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
