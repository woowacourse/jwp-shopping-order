package cart.entity;

import cart.domain.cart.Product;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final long price;

    public ProductEntity(final String name, final String imageUrl, final long price) {
        this(null, name, imageUrl, price);
    }

    public ProductEntity(final Long id, final String name, final String imageUrl, final long price) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public static ProductEntity from(final Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice().getLongValue()
        );
    }

    public Product toDomain() {
        return new Product(id, name, imageUrl, price);
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

    public long getPrice() {
        return price;
    }
}
