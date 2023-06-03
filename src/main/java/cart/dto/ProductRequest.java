package cart.dto;

import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotNull(message = "상품 이름이 입력되지 않았습니다.")
    private String name;
    @NotNull(message = "상품 가격이 입력되지 않았습니다.")
    private Integer price;
    @NotNull(message = "상품 이미지 주소가 입력되지 않았습니다.")
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
