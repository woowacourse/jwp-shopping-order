package cart.product.dto;

import cart.product.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private Double pointRatio;
    private Boolean pointAvailable;
    
    public ProductResponse(final Long id, final String name, final Long price, final String imageUrl, final Double pointRatio, final Boolean pointAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
    }
    
    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getPointRatio(), product.getPointAvailable());
    }
    
    public Long getId() {
        return id;
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
