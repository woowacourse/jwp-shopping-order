package cart.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class ProductRequest {
    @NotBlank(message = "상품 이름은 반드시 포함되어야 합니다.")
    private String name;

    @NotNull(message = "가격은 반드시 포함되어야 합니다.")
    @PositiveOrZero(message = "가격은 음수가 될 수 없습니다.")
    private Long price;

    @NotBlank(message = "상품 이미지는 반드시 포함되어야 합니다.")
    private String imageUrl;

    private ProductRequest() {
    }

    public ProductRequest(String name, Long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
