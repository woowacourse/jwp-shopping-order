package cart.ui.controller.dto.response;

import cart.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 응답")
public class ProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품 이름", example = "치킨")
    private String name;

    @Schema(description = "상품 가격", example = "10000")
    private int price;

    @Schema(description = "상품 이미지 경로", example = "http://chicken.com")
    private String imageUrl;

    private ProductResponse() {
    }

    private ProductResponse(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse from(Product product) {
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
