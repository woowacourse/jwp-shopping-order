package cart.domain;

public class Product {

    private final Long id;
    private final String name;
    private final Money price;
    private final String imageUrl;

    public Product(Long id, String name, Money price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Product of(Long id, String name, int price, String imageUrl) {
        return new Product(id, name, new Money(price), imageUrl);
    }

    public static Product of(String name, int price, String imageUrl) {
        return new Product(null, name, new Money(price), imageUrl);
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
