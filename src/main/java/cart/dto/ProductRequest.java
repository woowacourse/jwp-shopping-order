package cart.dto;

import javax.validation.constraints.NotNull;

public class ProductRequest {
    @NotNull
    private String name;
    @NotNull
    private int price;
    @NotNull
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
