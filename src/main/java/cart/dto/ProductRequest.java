package cart.dto;

import java.math.BigDecimal;

public class ProductRequest {
    private String name;
    private BigDecimal price;
    private String imageUrl;

    public ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
