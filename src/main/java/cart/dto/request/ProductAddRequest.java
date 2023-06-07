package cart.dto.request;

public class ProductAddRequest {

    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Integer stock;

    private ProductAddRequest() {
        this(null, null, null, null);
    }

    public ProductAddRequest(String name, Integer price, String imageUrl, Integer stock) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getStock() {
        return stock;
    }
}
