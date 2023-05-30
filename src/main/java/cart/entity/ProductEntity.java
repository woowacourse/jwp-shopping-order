package cart.entity;

import cart.domain.Product;
import java.math.BigDecimal;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final String imageUrl;
    private final boolean isDeleted;

    public ProductEntity(String name, BigDecimal price, String imageUrl) {
        this(null, name, price, imageUrl, false);
    }

    public ProductEntity(Long id, String name, BigDecimal price, String imageUrl) {
        this(id, name, price, imageUrl, false);
    }

    public ProductEntity(Long id, String name, BigDecimal price, String imageUrl, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
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

    public boolean isDeleted() {
        return isDeleted;
    }
}
