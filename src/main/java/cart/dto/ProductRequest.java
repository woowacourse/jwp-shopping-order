package cart.dto;

public class ProductRequest {
    private String name;
    private int price;
    private String imageUrl;
    private int stock;

    public ProductRequest() {
    }

    public ProductRequest(String name, int price, String imageUrl, int stock) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
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

    public int getStock() {
        return stock;
    }
}
