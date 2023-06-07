package cart.domain;

import cart.exception.OrderException;

import java.util.Objects;

public class Product {

    private final Long id;
    private final String name;
    private final Price price;
    private final String imageUrl;
    private final int stock;

    public Product(final Long id, final String name, final Price price, final String imageUrl, final int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public Product(final String name, final Price price, final String imageUrl, final int stock) {
        this(null, name, price, imageUrl, stock);
    }

    public Product updateStock(final Quantity quantity) {
        validateStock(quantity);

        return new Product(id, name, price, imageUrl, stock - quantity.getValue());
    }

    private void validateStock(final Quantity quantity) {
        if (stock < quantity.getValue()) {
            throw new OrderException.InsufficientStock("상품의 남은 수량보다 많은 주문량입니다.");
        }
    }

    public int calculateTotalPrice(final int quantity) {
        Price result = price.calculateTotalPrice(quantity);

        return result.getValue();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price.getValue();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return stock == product.stock && Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, imageUrl, stock);
    }
}
