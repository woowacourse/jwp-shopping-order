package cart.domain;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderEntity {

    private final Long id;
    private final String dateTime;
    private final Long memberId;
    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final String productImageUrl;
    private final int quantity;
    private final int totalProductPrice;
    private final int totalDeliveryFee;
    private final int usePoint;
    private final int totalPrice;

    public OrderEntity(
            final String dateTime,
            final Long memberId,
            final Long productId,
            final String productName,
            final int productPrice,
            final String productImageUrl,
            final int quantity,
            final int totalProductPrice,
            final int totalDeliveryFee,
            final int usePoint,
            final int totalPrice
    ) {
        this(null, dateTime, memberId, productId, productName, productPrice, productImageUrl, quantity, totalProductPrice, totalDeliveryFee, usePoint, totalPrice);
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
