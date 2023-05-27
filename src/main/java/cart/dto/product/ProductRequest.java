package cart.dto.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotBlank(message = "상품의 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "상품의 가격을 입력해주세요.")
    private Integer price;

    @NotBlank(message = "상품의 이미지를 입력해주세요.")
    private String imageUrl;

    public ProductRequest() {
    }

    public ProductRequest(final String name, final int price, final String imageUrl) {
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
