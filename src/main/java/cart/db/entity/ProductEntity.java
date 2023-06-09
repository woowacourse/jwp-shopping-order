package cart.db.entity;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final boolean isDeleted;

    public ProductEntity(final Long id, final String name, final Integer price, final String imageUrl, final boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
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

    public boolean getIsDeleted() {
        return isDeleted;
    }
}
