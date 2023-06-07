package cart.domain;

import cart.exception.OrderException;

import java.util.Objects;

public class Product {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int stock;

    public Product(final Long id, final String name, final int price, final String imageUrl, final int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public Product(final String name, final int price, final String imageUrl, final int stock) {
        this(null, name, price, imageUrl, stock);
    }

    public Product updateStock(final int quantity) {
        validateStock(quantity);

        return new Product(id, name, price, imageUrl, stock - quantity);
    }

    private void validateStock(final int quantity) {
        if (stock < quantity) {
            throw new OrderException.InsufficientStock("상품의 남은 수량보다 많은 주문량입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
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
        return price == product.price && stock == product.stock && Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, imageUrl, stock);
    }
}
