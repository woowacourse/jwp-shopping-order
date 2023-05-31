package cart.dao.entity;

import cart.domain.CartItem;

public class CartItemEntity {

    private Long id;
    private long memberId;
    private long productId;
    private int quantity;

    public CartItemEntity(long memberId, long productId, int quantity) {
        this(null, memberId, productId, quantity);
    }

    public CartItemEntity(Long id, long memberId, long productId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static CartItemEntity fromDomain(CartItem cartItem) {
        return new CartItemEntity(
            cartItem.getId(),
            cartItem.getMember().getId(),
            cartItem.getProduct().getId(),
            cartItem.getQuantityCount());
    }

    public Long getId() {
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
}
