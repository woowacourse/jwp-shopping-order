package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    private Long id;
    private Integer quantity;
    private final Product product;
    private final Member member;

    private CartItem(Long id, Integer quantity, Product product, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public static CartItem createInitCartItem(Member member, Product product) {
        return new CartItem(null, 1, product, member);
    }

    public static CartItem of(Long id, Integer quantity, Product product, Member member) {
        return new CartItem(id, quantity, product, member);
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public boolean checkSameCartItem(CartItem cartItem) {
        return this.product.equals(cartItem.getProduct());
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
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

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id) && Objects.equals(quantity, cartItem.quantity) && Objects.equals(product, cartItem.product) && Objects.equals(member, cartItem.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, product, member);
    }
}
