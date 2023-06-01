package cart.domain;

public class Product {
    private final Long id;
    private final String name;
    private final Price price;
    private final String imageUrl;

    public Product(String name, int price, String imageUrl) {
        this(null, name, new Price(price), imageUrl);
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this(id, name, new Price(price), imageUrl);
    }

    public Product(Long id, String name, Price price, String imageUrl) {
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

    public int getPrice() {
        return price.getValue();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
