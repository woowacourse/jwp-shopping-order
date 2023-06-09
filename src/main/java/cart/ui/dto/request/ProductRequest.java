package cart.ui.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class ProductRequest {

    @NotBlank(message = "제품 이름이 입력되지 않았습니다.")
    private String name;
    @Positive(message = "제품 가격은 음수일 수 없습니다.")
    private Integer price;
    @NotBlank(message = "제품 사진이 입력되지 않았습니다.")
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
