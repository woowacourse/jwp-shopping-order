package cart.dto.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotBlank(message = "상품명은 비어있을 수 없습니다.")
    private String name;
    @NotNull(message = "상품 가격은 비어있을 수 없습니다.")
    private Integer price;
    @NotBlank(message = "이미지url은 비어있을 수 없습니다.")
    private String imageUrl;

    public ProductRequest() {
    }

    public ProductRequest(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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
