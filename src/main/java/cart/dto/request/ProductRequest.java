package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProductRequest {

    @NotNull(message = "상품의 이름을 입력해 주세요. 입력값 : ${validatedValue}")
    private String name;

    @NotNull(message = "상품의 가격을 입력해 주세요. 입력값 : ${validatedValue}")
    @Positive(message = "상품의 가격은 1 이상으로 입력해 주세요. 입력값 : ${validatedValue}")
    private Integer price;

    @NotNull(message = "상품의 이미지 URL을 입력해 주세요. 입력값 : ${validatedValue}")
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
