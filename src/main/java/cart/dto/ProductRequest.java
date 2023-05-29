package cart.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.URL;

public class ProductRequest {

    @NotBlank(message = "상품 이름을 입력해주세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z]*$", message = "상품 이름은 한글 또는 영어여야합니다.")
    private String name;

    @Positive(message = "가격은 양수여야합니다.")
    private int price;

    @NotBlank(message = "상품 이미지 URL을 입력해주세요.")
    @URL(message = "올바른 URL 형식으로 입력해주세요.")
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
