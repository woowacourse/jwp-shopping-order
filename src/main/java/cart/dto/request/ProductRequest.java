package cart.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class ProductRequest {
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String name;
    @NotNull(message = "상품가격은 필수 입력 값입니다.")
    @PositiveOrZero(message = "상품가격은 0 이상이어야 합니다.")
    private int price;
    @NotBlank(message = "이미지 Url은 필수 입력 값입니다.")
    private String imageUrl;

    public ProductRequest() {
    }

    public ProductRequest(String name, int price, String imageUrl) {
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
