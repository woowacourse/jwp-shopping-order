package cart.repository.entity;

import java.time.LocalDateTime;

public class ProductEntity {
    private final Long id;
    private final String name;
    private final int price;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ProductEntity(final Long id, final String name, final int price, final LocalDateTime createdAt,
                         final LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
