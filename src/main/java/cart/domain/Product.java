package cart.domain;

public class Product {

    private final Long id;
    private final String name;
    private final Price price;
    private final String imageUrl;

    public Product(final Long id, final String name, final Price price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(final String name, final Price price, final String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
