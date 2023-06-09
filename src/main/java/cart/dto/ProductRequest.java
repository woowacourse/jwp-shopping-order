package cart.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class ProductRequest {
    @NotBlank(message = "상품 이름은 공백이 될 수 없습니다.")
    private String name;
    @Positive(message = "상품 가격은 양수여야 합니다.")
    private int price;
    @NotBlank(message = "상품 이미지는 공백이 될 수 없습니다.")
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
