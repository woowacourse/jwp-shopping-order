package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 정보 추가/수정 요청")
public class ProductRequest {

    @Schema(description = "상품 이름", example = "치킨")
    private String name;
    @Schema(description = "상품 가격", example = "10000")
    private int price;
    @Schema(description = "상품 이미지 url", example = "http://www.example.com/chicken.jpg")
    private String imageUrl;

    public ProductRequest() {
    }

    public ProductRequest(final String name, final int price, final String imageUrl) {
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
