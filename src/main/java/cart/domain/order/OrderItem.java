package cart.domain.order;

import cart.domain.product.Product;

import java.util.Objects;

public class OrderItem {

    private final Product product;
    private final int quantity;

    public OrderItem(final Product product, final int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return product.getPriceValue() * quantity;
    }

    public Long getProductId() {
        return product.getId();
    }

    public int getProductPriceValue() {
        return product.getPriceValue();
    }

    public String getProductNameValue() {
        return product.getNameValue();
    }

    public String getProductImageUrlValue() {
        return product.getImageUrlValue();
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity && Objects.equals(product, orderItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity);
    }
}
