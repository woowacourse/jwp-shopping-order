package cart.entity;

public class CartItem {
    private final Long id;
    private final int quantity;
    private final Long productId;
    private final Long memberId;

    public CartItem(Long id, int quantity, Long productId, Long memberId) {
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
