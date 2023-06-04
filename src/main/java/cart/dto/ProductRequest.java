package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ProductRequest {
    @Schema(description = "상품 ID")
    private String name;
    @Schema(description = "상품 가격")
    private int price;
    @Schema(description = "상품 이미지 주소")
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
