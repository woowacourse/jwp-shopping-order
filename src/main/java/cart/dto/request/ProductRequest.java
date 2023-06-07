package cart.dto.request;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ProductRequest {
    @NotNull
    private String name;

    @NotNull
    @Positive
    private int price;

    @NotNull
    @URL
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
