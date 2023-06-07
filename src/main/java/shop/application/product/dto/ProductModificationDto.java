package shop.application.product.dto;

public class ProductModificationDto {
    private final String name;
    private final int price;
    private final String imageUrl;

    public ProductModificationDto(String name, int price, String imageUrl) {
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
