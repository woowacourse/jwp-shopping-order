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

    public ProductRequest(final String name, final Integer price, final String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product toDomain() {
        return new Product(name, price, imageUrl);
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
