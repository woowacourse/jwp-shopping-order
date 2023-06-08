package cart.domain;

import java.util.Objects;

public class OrderItem {

    private Long id;
    private final Product product;
    private final Integer quantity;

    private OrderItem(Long id, Product product, Integer quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public static OrderItem createInitOrderItem(Product product, Integer quantity) {
        return new OrderItem(null, product, quantity);
    }

    public static OrderItem of(Long id, Product product, Integer quantity) {
        return new OrderItem(id, product, quantity);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return this.product.getName();
    }

    public Integer getProductPrice() {
        return this.product.getPrice();
    }

    public String getProductUrl() {
        return this.product.getImageUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) && Objects.equals(product, orderItem.product) && Objects.equals(quantity, orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, quantity);
    }
}
