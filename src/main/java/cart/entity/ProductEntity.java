package cart.entity;

import cart.domain.Product;
import java.math.BigDecimal;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final String imageUrl;

    public ProductEntity(Long id, String name, BigDecimal price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product toDomain() {
        return new Product(id, name, price, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
