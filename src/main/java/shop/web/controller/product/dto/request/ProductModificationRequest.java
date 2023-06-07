package shop.web.controller.product.dto.request;

public class ProductModificationRequest {
    private String name;
    private int price;
    private String imageUrl;

    private ProductModificationRequest() {
    }

    public ProductModificationRequest(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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
}
