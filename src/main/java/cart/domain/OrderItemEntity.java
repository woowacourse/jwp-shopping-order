package cart.domain;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderItemEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final String productImageUrl;
    private final int quantity;

    public OrderItemEntity(Long orderId, Long productId, String productName, int productPrice, String productImageUrl, int quantity) {
        this(null, orderId, productId, productName, productPrice, productImageUrl, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity that = (OrderItemEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
