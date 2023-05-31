package cart.ui.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "상품 생성 요청")
public class ProductRequest {

    @Schema(description = "상품 이름", example = "치킨")
    @NotBlank(message = "상품 이름은 비어있을 수 없습니다.")
    private String name;

    @Schema(description = "상품 가격", example = "10000")
    @NotNull(message = "상품 가격은 존재해야 합니다.")
    private Integer price;

    @Schema(description = "상품 이미지 경로", example = "http://chicken.com")
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
