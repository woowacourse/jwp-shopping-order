package cart.persistence.entity;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final Integer price;
    private final Boolean isDeleted;

    public ProductEntity(final String name, final String imageUrl, final Integer price, final Boolean isDeleted) {
        this(null, name, imageUrl, price, isDeleted);
    }

    public ProductEntity(final Long id, final String name, final String imageUrl, final Integer price,
                         final Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }
}
