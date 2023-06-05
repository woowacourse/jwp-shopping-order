package cart.entity;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final String image;
    private final Long price;

    public ProductEntity(
            final Long id,
            final String name,
            final String image,
            final long price
    ) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public Long getPrice() {
        return price;
    }
}
