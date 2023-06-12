package cart.dto.product;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static cart.domain.product.Product.MAXIMUM_NAME_LENGTH;
import static cart.domain.product.Product.MINIMUM_NAME_LENGTH;

public class ProductRequest {

    @NotNull(message = "상품 이름을 입력해야 합니다.")
    @Size(min = MINIMUM_NAME_LENGTH, max = MAXIMUM_NAME_LENGTH, message = "상품 이름은 " + MINIMUM_NAME_LENGTH + " 이상 " + MAXIMUM_NAME_LENGTH + " 이하입니다. 입력값: ${validatedValue}")
    private String name;

    private int price;

    @NotNull(message = "이미지 주소를 입력해야 합니다.")
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
