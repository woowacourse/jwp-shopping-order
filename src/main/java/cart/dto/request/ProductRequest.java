package cart.dto.request;

import java.beans.ConstructorProperties;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class ProductRequest {

    @NotBlank
    private String name;
    @NotNull
    @Range(min = 1)
    private int price;
    @NotBlank
    private String imageUrl;

    @ConstructorProperties(value = {"name", "price", "imageUrl"})
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
