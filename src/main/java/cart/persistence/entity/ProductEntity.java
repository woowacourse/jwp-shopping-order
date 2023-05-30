package cart.persistence.entity;

import java.time.LocalDateTime;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final LocalDateTime createdAt;

    public ProductEntity(final Long id, final String name, final Integer price, final String imageUrl, final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
