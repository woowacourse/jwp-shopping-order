package cart.domain.cart;

import cart.domain.product.Product;
import cart.exception.QuantityExceedsCartException;

import java.util.Objects;

public class CartItem {

    private static final int DEFAULT_QUANTITY = 1;
    private static final int EMPTY = 0;

    private Long id;
    private final Product product;
    private int quantity;

    public CartItem(final Product product) {
        this.product = product;
        this.quantity = DEFAULT_QUANTITY;
    }

    public CartItem(final Long id, final Product product, final int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public void validateQuantity(final int quantity) {
        if (this.quantity < quantity) {
            throw new QuantityExceedsCartException();
        }
    }

    public int getAppliedDiscountOrOriginPrice(final int quantity) {
        return this.product.getAppliedDiscountOrOriginPrice() * quantity;
    }

    public int getAppliedDiscountOrOriginPrice() {
        return this.product.getAppliedDiscountOrOriginPrice();
    }

    public boolean isEmptyQuantity() {
        return this.quantity == EMPTY;
    }

    public void buy(final int quantity) {
        this.quantity -= quantity;
    }

    public boolean hasProduct(final Long id) {
        return this.product.isSame(id);
    }

    public boolean isSame(final long id) {
        return this.id == id;
    }

    public boolean isExistAlready() {
        return this.id != null;
    }

    public void addQuantity() {
        quantity++;
    }

    public void changeQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getOriginPrice() {
        return this.product.getPrice();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
