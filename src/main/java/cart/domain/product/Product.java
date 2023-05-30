package cart.domain.product;

public class Product {
    private final Long id;
    private final ProductName name;
    private final ProductPrice price;
    private final String imageUrl;

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = new ProductName(name);
        this.price = new ProductPrice(price);
        this.imageUrl = imageUrl;
    }

    public Product(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public int getPrice() {
        return price.getPrice();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
