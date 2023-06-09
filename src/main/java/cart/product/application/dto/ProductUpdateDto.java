package cart.product.application.dto;

public class ProductUpdateDto {

    private String name;
    private Integer price;
    private String imageUrl;
    private Integer stock;

    public ProductUpdateDto(String name, Integer price, String imageUrl, Integer stock) {
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
