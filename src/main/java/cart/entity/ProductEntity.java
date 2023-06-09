package cart.entity;

import cart.domain.Product;

public class ProductEntity {
    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;

    public ProductEntity(final Long id, final String name, final Integer price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ProductEntity(final String name, final Integer price, final String imageUrl) {
        this(null, name, price, imageUrl);
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

    public Product toProduct() {
        return new Product(id, name, price, imageUrl);
    }
}
