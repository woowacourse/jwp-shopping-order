package cart.application.service.product.dto;

import cart.ui.product.dto.ProductRequest;

public class ProductCreateDto {

    private final String name;
    private final int price;
    private final String imageUrl;

    public ProductCreateDto(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductCreateDto from(ProductRequest productRequest) {
        return new ProductCreateDto(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
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
