package cart.domain.cart;

import cart.domain.member.Member;
import cart.domain.product.Price;
import cart.domain.product.Product;
import cart.exception.CartItemException;

public class CartItem {
    private Long id;
    private Quantity quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        this.quantity = Quantity.minQuantity();
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, Quantity quantity, Product product, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public void checkOwner(final Member member) {
        if (this.member.isNotSameMember(member)) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(final Quantity quantity) {
        this.quantity = quantity;
    }

    public Price getTotalPrice() {
        return product.getPrice().multiply(quantity.quantity());
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

    public Quantity getQuantity() {
        return quantity;
    }
}
