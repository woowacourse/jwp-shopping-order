package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {
    private static final int MINIMUM_QUANTITY = 1;

    private final Long id;
    private final Member member;
    private final Product product;
    private Integer quantity;

    public CartItem(final Member member, final Product product) {
        this(null, member, product, MINIMUM_QUANTITY);
    }

    public CartItem(final Long id, final Member member, final Product product, final Integer quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
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

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember();
        }
    }

    public void changeQuantity(final int quantity) {
        if (quantity < MINIMUM_QUANTITY) {
            throw new IllegalArgumentException("변경 가능 최소 수량은 1입니다.");
        }
        this.quantity = quantity;
    }
}
