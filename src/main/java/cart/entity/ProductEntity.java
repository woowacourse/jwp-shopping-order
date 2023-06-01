package cart.entity;

import cart.domain.Product;

public class ProductEntity {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    public ProductEntity(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public ProductEntity(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductEntity from(Product product) {
        return new ProductEntity(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public Product toDomain() {
        return new Product(id, name, price, imageUrl);
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
