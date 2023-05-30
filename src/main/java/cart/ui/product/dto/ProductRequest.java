package cart.ui.product.dto;

public class ProductRequest {
    private String name;
    private Long price;
    private String imageUrl;

    public ProductRequest() {
    }

    public ProductRequest(String name, Long price, String imageUrl) {
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
