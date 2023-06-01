package cart.product.dto;

public class ProductRequest {
    private String name;
    private Long price;
    private String imageUrl;
    private Double pointRatio;
    private Boolean pointAvailable;

    public ProductRequest() {
    }
    
    public ProductRequest(final String name, final Long price, final String imageUrl, final Double pointRatio, final Boolean pointAvailable) {
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
