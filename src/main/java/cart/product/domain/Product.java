package cart.product.domain;

import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    private Product(final Long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Product of(final String name, final int price, final String imageUrl) {
        return new Product(null, name, price, imageUrl);
    }

    public static Product of(final Long id, final String name, final int price, final String imageUrl) {
        return new Product(id, name, price, imageUrl);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
}
