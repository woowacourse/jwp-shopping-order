package cart.cartItem.application;

import cart.exception.auth.AuthorizationException;
import cart.exception.order.CartItemQuantityDoesNotMatchException;
import cart.member.application.Member;
import cart.product.application.Product;
import java.util.Objects;

public class CartItem {
    private Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
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

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new AuthorizationException("해당 member의 cartItem이 아닙니다.");
        }
    }

    public void checkQuantity(int quantity) {
        if (!Objects.equals(this.quantity, quantity)) {
            throw new CartItemQuantityDoesNotMatchException();
        }
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

    public int getQuantity() {
        return quantity;
    }
}
