package cart.entity;

import cart.domain.product.Product;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final String image;
    private final int price;

    public ProductEntity(
            final Long id,
            final String name,
            final String image,
            final int price
    ) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public static ProductEntity from(final Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName().name(),
                product.getImage().imageUrl(),
                product.getPrice().price()
        );
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

    public int getPrice() {
        return price;
    }
}
