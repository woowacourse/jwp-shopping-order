package cart.controller.dto.response;

import cart.entity.ProductEntity;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    private ProductResponse(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse of(ProductEntity productEntity) {
        return new ProductResponse(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl());
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
