package cart.domain;

import java.util.Objects;

public class Product {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    public Product(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product other = (Product) o;
        return price == other.price
                && Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(imageUrl, other.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, imageUrl);
    }
}
