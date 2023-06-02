package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    public static final int MINIMUM_QUANTITY = 1;

    private final Long id;
    private final Product product;
    private final Member member;
    private int quantity;

    public CartItem(Member member, Product product) {
        this(0L, MINIMUM_QUANTITY, product, member);
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        validate(member, product, quantity);
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    private void validate(Member member, Product product, int quantity) {
        validateMember(member);
        validateQuantity(quantity);
        validateProduct(product);
    }

    private void validateQuantity(int quantity) {
        if (quantity < MINIMUM_QUANTITY) {
            throw new CartItemException.InvalidQuantity(this);
        }
    }

    private void validateProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new CartItemException.InvalidProduct(null);
        }
    }

    private void validateMember(Member member) {
        if (Objects.isNull(member)) {
            throw new CartItemException.InvalidMember(null);
        }
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.InvalidMember(member.getId());
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
