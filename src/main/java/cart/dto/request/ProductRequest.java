package cart.dto.request;

import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotNull
    private String name;
    @NotNull
    private Long price;
    @NotNull
    private String imageUrl;

    public ProductRequest() {
    }

    public ProductRequest(String name, Long price, String imageUrl) {
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
