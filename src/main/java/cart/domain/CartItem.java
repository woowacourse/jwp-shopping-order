package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    public static final int MINIMUM_QUANTITY = 1;
    private static final int MINUMUM_QUANTITY = 1;

    private Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        validate(member, product);
        this.quantity = MINUMUM_QUANTITY;
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        validate(id, quantity, member, product);
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    private void validate(Member member, Product product) {
        validateMember(member);
        validateProduct(product);
    }

    private void validate(long id, int quantity, Member member, Product product) {
        validateId(id);
        validateQuantity(quantity);
        validate(member, product);
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
