package cart.product.repository;

public class ProductEntity {
    private final Long id;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private final Double pointRatio;
    private final boolean pointAvailable;
    
    public ProductEntity(final Long id, final String name, final Long price, final String imageUrl, final Double pointRatio, final boolean pointAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
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
    
    public boolean isPointAvailable() {
        return pointAvailable;
    }
}
