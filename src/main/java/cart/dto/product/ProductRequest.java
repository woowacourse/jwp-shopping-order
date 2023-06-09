package cart.dto.product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotEmpty
    private String name;
    @NotNull
    @Min(value = 0, message = "상품가격은 양수여야 합니다")
    private Long price;
    @NotEmpty
    private String imageUrl;

    public ProductRequest() {
    }

    public ProductRequest(final String name, final Long price, final String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
