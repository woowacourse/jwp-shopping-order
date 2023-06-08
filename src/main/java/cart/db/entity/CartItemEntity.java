package cart.db.entity;

import cart.domain.product.CartItem;

public class CartItemEntity {

    private Long id;
    private Integer quantity;
    private ProductEntity product;
    private MemberEntity member;

    public CartItemEntity(Long id, Integer quantity, ProductEntity product, MemberEntity member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public static CartItemEntity of(CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductEntity.of(cartItem.getProduct()),
                MemberEntity.of(cartItem.getMember())
        );
    }

    public CartItem toDomain() {
        return new CartItem(
                id,
                quantity,
                product.toDomain(),
                member.toDomain()
        );
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public MemberEntity getMember() {
        return member;
    }
}
