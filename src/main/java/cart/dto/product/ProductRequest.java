package cart.dto.product;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotBlank(message = "상품의 이름은 비어있을 수 없습니다.")
    private String name;

    @Min(value = 0, message = "상품의 가격은 음수가 될 수 없습니다.")
    @Max(value = 1_000_000_000, message = "상품의 가격은 1억을 넘을 수 없습니다.")
    private int price;

    @NotBlank(message = "이미지는 비어있을 수 없습니다.")
    private String imageUrl;

    @NotNull(message = "할인 여부는 null이 될 수 없습니다.")
    private Boolean isDiscounted;

    @Min(value = 0, message = "할인율은 음수가 될 수 없습니다.")
    @Max(value = 100, message = "할인율은 100을 넘길 수 없습니다.")
    private int discountRate;

    public ProductRequest() {
    }

    public ProductRequest(final String name, final int price, final String imageUrl, final boolean isDiscounted, final int discountRate) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDiscounted = isDiscounted;
        this.discountRate = discountRate;
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

    public boolean getIsDiscounted() {
        return isDiscounted;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
