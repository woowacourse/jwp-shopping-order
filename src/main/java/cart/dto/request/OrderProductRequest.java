package cart.dto.request;

import cart.domain.Product;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OrderProductRequest {

    @NotNull(message = "상품 아이디를 입력해주세요.")
    private Long id;

    @NotBlank(message = "상품 명을 입력해주세요.")
    private String name;

    @NotNull(message = "상품 가격을 입력해주세요.")
    private Integer price;

    @NotBlank(message = "상품의 이미지 URL을 입력해주세요")
    private String imageUrl;

    public OrderProductRequest() {
    }

    public OrderProductRequest(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product toDomain() {
        return new Product(id, name, price, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
