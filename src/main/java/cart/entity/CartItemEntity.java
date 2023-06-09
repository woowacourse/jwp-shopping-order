package cart.entity;

import cart.domain.CartItem;

public class CartItemEntity {

    private Long id;
    private Long productId;
    private Long memberId;
    private Integer quantity;

    public CartItemEntity(Long productId, Long memberId, Integer quantity) {
        this(null, productId, memberId, quantity);
    }

    public CartItemEntity(Long id, Long productId, Long memberId, Integer quantity) {
        this.id = id;
        this.productId = productId;
        this.memberId = memberId;
        this.quantity = quantity;
    }

    public static CartItemEntity from(CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getMemberId(),
                cartItem.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
