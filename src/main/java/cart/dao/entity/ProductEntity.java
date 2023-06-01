package cart.dao.entity;

import cart.domain.Money;

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
