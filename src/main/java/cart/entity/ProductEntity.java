package cart.entity;

public class ProductEntity {
    private final Long id;
    private final String name;
    private final Integer Price;
    private final String imageUrl;

    public ProductEntity(final Long id, final String name, final Integer price, final String imageUrl) {
        this.id = id;
        this.name = name;
        Price = price;
        this.imageUrl = imageUrl;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return Price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
