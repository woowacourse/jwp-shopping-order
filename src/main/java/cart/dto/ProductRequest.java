package cart.dto;

import cart.domain.product.Product;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProductRequest {

    @NotBlank(message = "상품의 이름을 입력하세요")
    private String name;

    @NotNull(message = "상품의 가격을 입력하세요")
    @Positive(message = "상품의 가격은 1원 이상이어야 합니다")
    private Integer price;

    @NotBlank(message = "상품의 이미지 URL을 입력하세요")
    @URL
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
