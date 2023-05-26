package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    public static final int MINIMUM_QUANTITY = 1;

    private Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        this.quantity = 1;

        validateMember(member);
        this.member = member;

        validateProduct(product);
        this.product = product;
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        validateId(id);
        this.id = id;

        validateQuantity(quantity);
        this.quantity = quantity;

        validateProduct(product);
        this.product = product;

        validateMember(member);
        this.member = member;
    }

    private void validateId(Long id) {
        if (Objects.isNull(id)) {
            throw new CartItemException.InvalidIdByNull();
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < MINIMUM_QUANTITY) {
            throw new CartItemException.InvalidQuantity(this);
        }
    }

    private void validateProduct(Object object) {
        if (Objects.isNull(object)) {
            throw new CartItemException.InvalidProduct();
        }
    }

    private void validateMember(Member member) {
        if (Objects.isNull(member)) {
            throw new CartItemException.InvalidMember(this, member);
        }
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
            throw new CartItemException.InvalidMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
