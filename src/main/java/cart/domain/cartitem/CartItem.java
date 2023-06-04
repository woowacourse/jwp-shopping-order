package cart.domain.cartitem;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;

import java.util.Objects;

public class CartItem {

    private Long id;
    private Long quantity;
    private final Member member;
    private final Product product;

    public CartItem(Member member, Product product) {
        this.quantity = 1L;
        this.member = member;
        this.product = product;
    }

    public CartItem(Long quantity, Member member, Product product) {
        this.quantity = quantity;
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, Long quantity, Member member, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.member = member;
        this.product = product;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartException(ErrorCode.ILLEGAL_MEMBER);
        }
    }

    public void checkQuantity() {
        if (product.getStock() < quantity) {
            throw new CartException(ErrorCode.CART_ITEM_QUANTITY_EXCESS);
        }
    }

    public void changeQuantity(Long quantity) {
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

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
