package cart.dto;

public class ProductRequest {
    private String name;
    private Integer price;
    private String imageUrl;
    private Integer stock;

    public ProductRequest(String name, Integer price, String imageUrl, Integer stock) {
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
