package cart.dto.product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ProductRequest {

    @NotBlank(message = "이름은 필수 입력입니다. 반드시 입력해주세요.")
    private String name;
    @Min(value = 0, message = "가격은 음수일 수 없습니다. 유효한 가격을 입력해주세요.")
    private int price;
    @NotBlank(message = "이미지는 필수 입력입니다. 반드시 입력해주세요.")
    @Pattern(regexp = "^https?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$", message = "유효한 URL 형식이 아닙니다.")
    private String imageUrl;

    private ProductRequest() {
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
