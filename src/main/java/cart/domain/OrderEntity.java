package cart.domain;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderEntity {

    private final Long id;
    private final String createdAt;
    private final Long memberId;
    private final int totalProductPrice;
    private final int totalDeliveryFee;
    private final int usePoint;
    private final int totalPrice;

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
