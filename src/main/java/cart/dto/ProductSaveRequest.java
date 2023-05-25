package cart.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public class ProductSaveRequest {

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Size(min = 1, max = 100, message = "이름은 최소 {min}자 이상, {max}자 이하여야 합니다.")
    private final String name;

    @NotBlank(message = "이미지는 공백일 수 없습니다.")
    private final String imageUrl;

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
