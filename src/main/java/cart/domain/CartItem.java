package cart.domain;

import cart.exception.CartItemException;

public class CartItem {
    private final Product product;
    private final Member member;
    private Long id;
    private int quantity;

    public CartItem(Product product, Member member) {
        this.quantity = 1;
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = quantity;
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
        return quantity;
    }

    public void checkOwner(Member member) {
        if (!this.member.equals(member)) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Money calculateCartPrice() {
        return product.getPrice().multiply(quantity);
    }
}
