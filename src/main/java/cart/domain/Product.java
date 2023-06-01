package cart.domain;

import cart.dao.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;

public class Product {
    private final Long id;
    private final String name;
    private final Money price;
    private final String imageUrl;

    public Product(final Long id, final String name, final Money price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(final String name, final Money price, final String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public static Product from(final ProductEntity product) {
        return new Product(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl());
    }

    public static List<Product> from(final List<ProductEntity> products) {
        return products.stream()
                .map(Product::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
