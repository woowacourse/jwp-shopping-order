package cart.domain.carts;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    public static final int MINIMUM_QUANTITY = 1;

    private Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        this(0L, MINIMUM_QUANTITY, product, member);
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        validate(id, quantity, member, product);
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    private void validate(Long id, int quantity, Member member, Product product) {
        validateId(id);
        validateQuantity(quantity);
        validateMember(member);
        validateProduct(product);
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

    private void validateProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new CartItemException.InvalidProduct();
        }
    }

    private void validateMember(Member member) {
        if (Objects.isNull(member)) {
            throw new CartItemException.InvalidMember();
        }
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.InvalidMember();
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int calculateTotalProductsPrice() {
        return product.getPrice() * quantity;
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
