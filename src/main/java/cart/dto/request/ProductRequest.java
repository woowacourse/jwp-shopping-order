package cart.dto.request;

import cart.domain.Product;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotBlank(message = "숫자를 입력해주세요")
    private String name;

    @NotNull(message = "가격을 입력해주세요.")
    private Integer price;

    @NotBlank(message = "이미지 URL 입력해주세요")
    private String imageUrl;

    private ProductRequest() {
    }

    public ProductRequest(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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

    public Product toEntity() {
        return new Product(name, price, imageUrl);
    }
}
