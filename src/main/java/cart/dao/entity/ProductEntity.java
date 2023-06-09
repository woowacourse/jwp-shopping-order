package cart.dao.entity;

import cart.domain.Money;
import cart.domain.Product;
import java.util.List;
import java.util.stream.Collectors;

public class ProductEntity {
    private final Long id;
    private final String name;
    private final Money price;
    private final String imageUrl;

    public ProductEntity(final Long id, final String name, final Money price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ProductEntity(final String name, final Money price, final String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public static List<Product> createAll(final List<ProductEntity> products) {
        return products.stream()
                .map(ProductEntity::create)
                .collect(Collectors.toList());
    }

    public Product create() {
        return new Product(id, name, price, imageUrl);
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
