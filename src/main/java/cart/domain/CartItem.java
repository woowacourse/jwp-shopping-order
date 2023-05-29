package cart.domain;

import cart.exception.CartItemException;
import cart.exception.ErrorMessage;

public class CartItem {
    private final Product product;
    private final Member member;
    private final Long id;
    private final Quantity quantity;

    public CartItem(Member member, Product product) {
        this(null, 0, product, member);
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = new Quantity(quantity);
        this.product = product;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity.getValue();
    }

    public void checkOwner(Member member) {
        if (!this.member.equals(member)) {
            throw new CartItemException(ErrorMessage.INVALID_CART_ITEM_OWNER);
        }
    }

    public CartItem changeQuantity(int quantity) {
        return new CartItem(id, quantity, product, member);
    }
}
