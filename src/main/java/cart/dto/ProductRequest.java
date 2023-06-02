package cart.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductRequest {
    @NotBlank(message = "상품 이름을 입력해주세요")
    private String name;
    @NotNull
    @Min(value = 0, message = "{value} 이상의 값을 입력해주세요")
    private int price;
    @NotBlank(message = "상품 이미지 주소를 입력해주세요")
    private String imageUrl;

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
