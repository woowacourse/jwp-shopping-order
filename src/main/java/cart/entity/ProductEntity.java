package cart.entity;

import cart.domain.Product;
import cart.domain.vo.Amount;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    private ProductEntity(final Long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductEntity from(final Product product) {
        return new ProductEntity(product.getId(), product.getName(), product.getAmount().getValue(),
                product.getImageUrl());
    }

    public static ProductEntity of(final long id, final String name, final int price, final String imageUrl) {
        return new ProductEntity(id, name, price, imageUrl);
    }

    public Product toDomain() {
        return new Product(id, name, Amount.of(price), imageUrl);
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
}
