package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProductRequest {

    @NotNull(message = "name 필드가 필요합니다.")
    private String name;

    @NotNull(message = "price 필드가 필요합니다.")
    @Positive(message = "상품의 가격은 양수여야 합니다.")
    private Integer price;

    @NotNull(message = "imageUrl 필드가 필요합니다.")
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
