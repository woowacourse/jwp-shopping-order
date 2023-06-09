package cart.cartitem.domain;

import cart.cartitem.exception.CartItemException;
import cart.member.domain.Member;
import cart.product.domain.Product;

import java.util.Objects;

public class CartItem {
    private Long id;
    private int quantity;
    private Product product;
    private Member member;

    private CartItem(final Long id, final int quantity, final Product product, final Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public static CartItem of(final Member member, final Product product) {
        return new CartItem(null, 1, product, member);
    }

    public static CartItem of(final Long id, final int quantity, final Product product, final Member member) {
        return new CartItem(id, quantity, product, member);
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException("해당 유저가 접근할 수 없는 장바구니 상품입니다");
        }
    }

    public void changeQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(final int quantity) {
        this.quantity += quantity;
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
