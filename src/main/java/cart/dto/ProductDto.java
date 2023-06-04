package cart.dto;

import cart.domain.product.Product;

import javax.validation.constraints.NotNull;

public class ProductDto {

    @NotNull(message = "id 필드가 필요합니다.")
    private Long id;

    @NotNull(message = "name 필드가 필요합니다.")
    private String name;

    @NotNull(message = "price 필드가 필요합니다.")
    private Integer price;

    @NotNull(message = "상품 이미지 정보가 필요합니다.")
    private String imageUrl;

    private ProductDto(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductDto of(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
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
