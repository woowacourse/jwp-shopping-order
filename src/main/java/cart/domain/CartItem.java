package cart.domain;

public class CartItem {

    private final Long id;
    private final Long memberId;
    private final Long productId;

    public CartItem(final Long memberId, final Long productId) {
        this(null, memberId, productId);
    }

    public CartItem(final Long id, final Long memberId, final Long productId) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }
}
