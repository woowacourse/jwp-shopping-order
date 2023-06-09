package cart.ui.product.dto;

import cart.application.service.product.dto.ProductResultDto;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    private ProductResponse(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse of(ProductResultDto productResultDto) {
        return new ProductResponse(productResultDto.getId(), productResultDto.getName(), productResultDto.getPrice(), productResultDto.getImageUrl());
    }

    public Long getId() {
        return id;
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
