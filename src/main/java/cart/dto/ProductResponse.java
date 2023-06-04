package cart.dto;

import cart.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 정보 응답")
public class ProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private final Long id;
    @Schema(description = "상품 이름", example = "치킨")
    private final String name;
    @Schema(description = "상품 가격", example = "10000")
    private final int price;
    @Schema(description = "상품 이미지 url", example = "http://www.example.com/chciken.jpg")
    private final String imageUrl;

    public ProductResponse(final Long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public Long getId() {
        return id;
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
