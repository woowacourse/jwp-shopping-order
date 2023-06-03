package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    private Long id;
    private int quantity;
    private Product product;
    private Member member;

    public CartItem(Member member, Product product) {
        this(null, 1, member, product);
    }

    public CartItem(Long id, int quantity, Member member, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember();
        }
    }

    public void changeQuantity(int quantity) {
        validateQuantity(quantity);

        this.quantity = quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new CartItemException.InvalidQuantity();
        }
    }

    public Money calculateCartItemPrice() {
        return product
                .price()
                .multiply(quantity);
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
