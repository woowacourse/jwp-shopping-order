package cart.dto.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class ProductRequest {
    @NotBlank(message = "상품의 이름은 빈 값이 아니어야 합니다.")
    private String name;

    @Positive(message = "상품의 가격은 1이상의 값이어야 합니다.")
    private int price;

    @NotBlank(message = "상품의 이미지 주소는 빈 값이 아니어야 합니다.")
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
