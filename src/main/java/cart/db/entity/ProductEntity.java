package cart.db.entity;

import cart.domain.product.Product;

public class ProductEntity {

    private Long id;
    private String productName;
    private Integer price;
    private String imageUrl;

    public ProductEntity(Long id, String productName, Integer price, String imageUrl) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductEntity of(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }

    public Product toDomain() {
        return new Product(id, productName, price, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
