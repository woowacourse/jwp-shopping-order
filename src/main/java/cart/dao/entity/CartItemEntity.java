package cart.dao.entity;

import java.util.Objects;

public class CartItemEntity {
    private final long id;
    private final long memberId;
    private final long productId;
    private final int quantity;

    public CartItemEntity(long id, long memberId, long productId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemEntity cartItem = (CartItemEntity) o;
        return id == cartItem.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
