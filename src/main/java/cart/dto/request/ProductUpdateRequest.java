package cart.dto.request;

public class ProductUpdateRequest {
    private final String name;
    private final Integer price;
    private final String imageUrl;

    public ProductUpdateRequest(final String name, final Integer price, final String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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
}
