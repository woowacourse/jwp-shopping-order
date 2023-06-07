package cart.domain;

import java.util.Objects;

public class Product {
    private final Long id;
    private final String name;
    private final Price price;
    private final String imageUrl;

    public Product(Long id, String name, long price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = Price.from(price);
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
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price.getAmount();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
