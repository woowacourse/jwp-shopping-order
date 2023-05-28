package cart.domain;

public class Product {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final Boolean isDeleted;

    public Product(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl, null);
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this(id, name, price, imageUrl, null);
    }

    public Product(final Long id, final String name, final int price, final String imageUrl, final Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }
}
