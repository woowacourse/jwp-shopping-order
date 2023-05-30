package cart.dto;

public class ProductRequest {
    private String name;
    private long price;
    private String imageUrl;

    public ProductRequest() {
    }

    public ProductRequest(String name, long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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
}
