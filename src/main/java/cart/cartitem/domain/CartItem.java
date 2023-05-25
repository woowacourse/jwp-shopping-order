package cart.cartitem.domain;

import static cart.cartitem.exception.CartItemExceptionType.NO_AUTHORITY_UPDATE_ITEM;

import cart.cartitem.exception.CartItemException;
import cart.cartitem.exception.CartItemExceptionType;
import cart.member.domain.Member;
import cart.product.domain.Product;
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
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException(NO_AUTHORITY_UPDATE_ITEM);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
