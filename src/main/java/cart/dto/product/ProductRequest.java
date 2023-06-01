package cart.dto.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class ProductRequest {

    @NotBlank(message = "상품 이름을 입력해주세요.")
    private String name;
    @PositiveOrZero(message = "가격은 0원 이상 입력해주세요.")
    private long price;
    @NotBlank(message = "이미지 경로를 입력해주세요.")
    private String imageUrl;
    @Positive(message = "상품의 재고를 1개 이상 입력해주세요.")
    private long stock;

    public ProductRequest() {
    }

    public ProductRequest(String name, long price, String imageUrl, long stock) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getStock() {
        return stock;
    }
}
