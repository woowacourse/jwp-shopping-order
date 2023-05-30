package cart.dto;

import cart.domain.Product;

import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotNull(message = "상품의 이름을 입력하세요")
    private String name;

    @NotNull(message = "상품의 가격을 입력하세요")
    private Integer price;

    @NotNull(message = "상품의 이미지 URL을 입력하세요")
    private String imageUrl;

    private ProductRequest(final String name, final Integer price, final String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductRequest of(final String name, final Integer price, final String imageUrl) {
        return new ProductRequest(name, price, imageUrl);
    }

    public Product toDomain() {
        return Product.of(name, price, imageUrl);
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
}
