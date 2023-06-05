package cart.dto.request;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class ProductRequest {

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String name;
    @Positive(message = "가격은 양수여야 합니다.")
    private int price;
    @NotBlank(message = "이미지 주소는 공백일 수 없습니다.")
    @URL(message = "이미지 주소는 URL 형식이어야 합니다.")
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
