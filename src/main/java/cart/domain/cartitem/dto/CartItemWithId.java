package cart.domain.cartitem.dto;

import cart.domain.member.dto.MemberWithId;
import cart.domain.product.dto.ProductWithId;

public class CartItemWithId {

    private final Long id;
    private final int quantity;
    private final ProductWithId product;
    private final MemberWithId member;

    public CartItemWithId(final Long id, final int quantity, final ProductWithId product, final MemberWithId member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductWithId getProduct() {
        return product;
    }

    public MemberWithId getMember() {
        return member;
    }
}
