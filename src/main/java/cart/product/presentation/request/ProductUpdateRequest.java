package cart.product.presentation.request;

import cart.product.application.dto.ProductUpdateDto;

public class ProductUpdateRequest {
    private String name;
    private Integer price;
    private String imageUrl;
    private Integer stock;

    public ProductUpdateRequest(String name, Integer price, String imageUrl, Integer stock) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public ProductUpdateDto toDto() {
        return new ProductUpdateDto(name, price, imageUrl, stock);
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
