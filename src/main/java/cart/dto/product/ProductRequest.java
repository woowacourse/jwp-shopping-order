package cart.dto.product;

public class ProductRequest {
    private String name;
    private Long price;
    private String imageUrl;

    public ProductRequest() {
    }

    public ProductRequest(final String name, final Long price, final String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
