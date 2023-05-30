package cart.dao.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class ProductEntity {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ProductEntity(final Long id, final String name, final int price, final String imageUrl,
                         final LocalDateTime createdAt,
                         final LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ProductEntity(final Long id, final String name, final int price, final String imageUrl) {
        this(id, name, price, imageUrl, null, null);
    }

    public static ProductEntity createForSave(final String name, final int price, final String imageUrl) {
        return new ProductEntity(null, name, price, imageUrl, null, null);
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductEntity that = (ProductEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
