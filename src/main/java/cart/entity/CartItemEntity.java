package cart.entity;

import cart.domain.CartItem;

public class CartItemEntity {

    private final Long id;
    private final Long memberId;
    private final Long productId;
    private final int quantity;

    private CartItemEntity(final Long id, final Long memberId, final Long productId, final int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static CartItemEntity from(final CartItem cartItem) {
        return new CartItemEntity(cartItem.getId(), cartItem.getMember().getId(), cartItem.getProduct().getId(),
                cartItem.getQuantity());
    }

    public static CartItemEntity of(final long id, final long memberId, final long productId, final int quantity) {
        return new CartItemEntity(id, memberId, productId, quantity);
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

    public int getQuantity() {
        return quantity;
    }
}
