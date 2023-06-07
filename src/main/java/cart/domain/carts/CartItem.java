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
        validate(quantity, member, product);
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    private void validate(int quantity, Member member, Product product) {
        validateQuantity(quantity);
        validateMember(member);
        validateProduct(product);
    }

    private void validateQuantity(int quantity) {
        if (quantity < MINIMUM_QUANTITY) {
            throw new CartItemException.InvalidQuantity(this);
        }
    }

    private void validateMember(Member member) {
        if (Objects.isNull(member)) {
            throw new CartItemException.InvalidMember();
        }
    }

    private void validateProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new CartItemException.InvalidProduct();
        }
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.InvalidMember();
        }
    }

    public void changeQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public int calculateTotalProductsPrice() {
        return product.getPrice() * quantity;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", product=" + product +
                ", member=" + member +
                '}';
    }
}
