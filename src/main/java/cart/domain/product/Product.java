package cart.domain.product;

public class Product {
    private final String name;
    private final Price price;
    private final String imageUrl;
    private Long id;

    public Product(final String name, final int price, final String imageUrl) {
        this.name = name;
        this.price = new Price(price);
        this.imageUrl = imageUrl;
    }

    public Product(final Long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price.getValue();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
