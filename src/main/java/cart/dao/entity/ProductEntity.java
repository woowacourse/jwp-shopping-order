package cart.dao.entity;

public class ProductEntity {

    private final Long id;
    private final int price;
    private final String name;
    private final String imageUrl;

    public ProductEntity(final Long id, final int price, final String name, final String imageUrl) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
