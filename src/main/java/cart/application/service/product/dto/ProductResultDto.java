package cart.application.service.product.dto;

import cart.domain.Product;

public class ProductResultDto {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    private ProductResultDto(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResultDto of(Product product) {
        return new ProductResultDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
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
