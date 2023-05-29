package cart.domain.cart;

import cart.domain.product.Product;

import java.util.Objects;

public class CartItem {

    private Long id;
    private final Product product;
    private int quantity;

    public CartItem(final Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public CartItem(final Long id, final Product product, final int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public boolean hasProduct(final Product product) {
        return this.product.equals(product);
    }

    public void validateQuantity(final int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("요청하신 수량이 장바구니에 담긴 수량보다 큽니다.");
        }
    }

    public int getFinallyPrice(final int quantity) {
        return this.product.getAppliedDiscountPrice() * quantity;
    }

    public boolean isEmptyQuantity() {
        return this.quantity == 0;
    }

    public void buy(final int quantity) {
        this.quantity -= quantity;
    }

    public boolean hasProduct(final Long id) {
        return this.product.isSame(id);
    }

    public boolean hasSameId(final long id) {
        return this.id == id;
    }

    public void addQuantity() {
        quantity++;
    }

    public void changeQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public boolean isExistAlready() {
        return this.id != null;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getApplyDiscountPrice() {
        return this.product.getAppliedDiscountPrice();
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
