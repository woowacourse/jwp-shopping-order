package cart.domain;

import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private Amount price;
    private String imageUrl;

    public Product(String name, Amount price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Product(Long id, String name, Amount price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId()) && Objects.equals(getName(), product.getName())
            && Objects.equals(getPrice(), product.getPrice()) && Objects.equals(getImageUrl(),
            product.getImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getImageUrl());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Amount getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
