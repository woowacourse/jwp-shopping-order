package cart.dao.entity;

import cart.domain.Money;
import cart.domain.Product;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final int price;

    public ProductEntity(String name, String imageUrl, int price) {
        this(null, name, imageUrl, price);
    }

    public ProductEntity(Long id, String name, String imageUrl, int price) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public static ProductEntity fromDomain(Product product) {
        return new ProductEntity(product.getId(), product.getName(), product.getImageUrl(), product.getPriceIntValue());
    }

    public Product toDomain() {
        return new Product(id, name, Money.from(price), imageUrl);
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

    public int getPrice() {
        return price;
    }
}
