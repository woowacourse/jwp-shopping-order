package cart.domain;

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
