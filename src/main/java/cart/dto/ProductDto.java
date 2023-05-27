package cart.dto;

import cart.domain.cart.Product;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품")
public class ProductDto {

    @Schema(description = "상품 Id", example = "1")
    private final Long id;

    @Schema(description = "상품명", example = "치즈피자")
    private final String name;

    @Schema(description = "이미지", example = "치즈피자.png")
    private final String imageUrl;

    @Schema(description = "가격", example = "8900")
    private final long price;

    public ProductDto(final Long id, final String name, final String imageUrl, final long price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductDto from(final Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice().getLongValue()
        );
    }

    public Long getId() {
        return id;
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
