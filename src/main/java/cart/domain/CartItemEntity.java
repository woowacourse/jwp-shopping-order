package cart.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Objects;

@AllArgsConstructor
@Getter
public class CartItemEntity {

    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final int quantity;

    public CartItemEntity(final Long memberId, final Long productId, final int quantity) {
        this(null, memberId, productId, quantity);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CartItemEntity that = (CartItemEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
