package cart.domain;

public class Product {
    private Long id;
    private String name;
    private Price price;
    private String imageUrl;

    public Product(String name, Price price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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

    public Price getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
