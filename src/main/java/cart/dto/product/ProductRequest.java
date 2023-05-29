package cart.dto.product;

public class ProductRequest {

    private String name;
    private long price;
    private String imageUrl;
    private long stock;

    public ProductRequest() {
    }

    public ProductRequest(String name, long price, String imageUrl, long stock) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getStock() {
        return stock;
    }
}
