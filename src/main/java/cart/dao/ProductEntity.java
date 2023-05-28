package cart.dao;

import cart.domain.Product;

public class ProductEntity {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    public ProductEntity(final String name, final int price, final String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public ProductEntity(final Long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductEntity from(final Product product) {
        return new ProductEntity(null, product.getName(), product.getPrice(), product.getImageUrl());
    }

    public static ProductEntity of(final Long productId, final Product product) {
        return new ProductEntity(productId, product.getName(), product.getPrice(), product.getImageUrl());
    }


    public Product toProduct() {
        return new Product(this.id, this.name, this.price, this.imageUrl);
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
