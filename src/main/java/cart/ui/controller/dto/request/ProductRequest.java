package cart.ui.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotBlank(message = "상품 이름은 비어있을 수 없습니다.")
    private String name;
    @NotNull(message = "상품 가격은 존재해야 합니다.")
    private Integer price;
    @NotBlank(message = "상품 이미지는 비어있을 수 없습니다.")
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
}
