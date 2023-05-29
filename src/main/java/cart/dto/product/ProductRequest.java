package cart.dto.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotBlank(message = "상품의 이름을 입력해주세요.")
    private String productName;

    @NotNull(message = "상품의 가격을 입력해주세요.")
    private Integer productPrice;

    @NotBlank(message = "상품의 이미지를 입력해주세요.")
    private String imageUrl;

    public ProductRequest() {
    }

    public ProductRequest(final String productName, final int productPrice, final String imageUrl) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
