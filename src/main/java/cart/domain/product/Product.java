package cart.domain.product;

public class Product {

    private final ProductName name;
    private final int price;
    private final String imageUrl;
    private final boolean isDeleted;

    public Product(final String name, final int price, final String imageUrl, final boolean isDeleted) {
        this.name = ProductName.create(name);
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return name.getName();
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
