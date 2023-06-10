package cart.presentation.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class ProductRequest {

    @NotBlank
    private String name;
    @Positive
    @NotNull
    private Long price;
    @NotBlank
    private String imageUrl;
    @PositiveOrZero
    @NotNull
    private Double pointRatio;
    @NotNull
    private Boolean pointAvailable;

    public ProductRequest() {
    }

    public ProductRequest(String name, Long price, String imageUrl, Double pointRatio, Boolean pointAvailable) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
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

    public Double getPointRatio() {
        return pointRatio;
    }

    public Boolean getPointAvailable() {
        return pointAvailable;
    }
}
