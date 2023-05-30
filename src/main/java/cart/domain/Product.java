package cart.domain;

public class Product {
    private final String name;
    private final Money price;
    private final String imageUrl;
    private Long id;

    public Product(String name, Money price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(Long id, String name, Money price, String imageUrl) {
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
