package cart.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

@Schema(description = "상품 저장 Request")
public class ProductSaveRequest {

    @Schema(description = "상품명", example = "파파존스 페페로니 피자")
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Size(min = 1, max = 100, message = "이름은 최소 {min}자 이상, {max}자 이하여야 합니다.")
    private final String name;

    @Schema(description = "상품 이미지", example = "https://imgcdn.pji.co.kr/pc/img/menu/detail/1011_10.png")
    @NotBlank(message = "이미지는 공백일 수 없습니다.")
    private final String imageUrl;

    @Schema(description = "상품 가격", example = "38500")
    @Range(message = "가격은 최소 {min}원 이상, {max}원 이하여야 합니다.")
    private final long price;

    public ProductSaveRequest(final String name, final String imageUrl, final long price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getPrice() {
        return price;
    }
}
