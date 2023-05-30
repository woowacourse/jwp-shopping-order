package cart.domain;

public class Product {
    private Long id;
    private String name;
    private Money price;
    private String imageUrl;

    public Product(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this(id, name, new Money(price), imageUrl);
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
