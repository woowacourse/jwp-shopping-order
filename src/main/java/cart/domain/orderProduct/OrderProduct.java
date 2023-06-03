package cart.domain.orderProduct;

import cart.domain.Model;
import cart.domain.product.Product;

import java.util.Objects;

public class OrderProduct implements Model {
    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Integer quantity;
    private final Product product;

    public OrderProduct(Long id, String name, Integer price, String imageUrl, Integer quantity, Product product) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.product = product;
    }

    public OrderProduct(String name, Integer price, String imageUrl, Integer quantity, Product product) {
        this(null, name, price, imageUrl, quantity, product);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProduct that = (OrderProduct) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
