package cart.dto;

import javax.validation.constraints.NotNull;

public class ProductRequest {
    @NotNull(message = "name은 null이 불가합니다")
    private String name;
    @NotNull(message = "price는 null이 불가합니다")
    private Integer price;
    @NotNull(message = "imageUrl은 null이 불가합니다")
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
