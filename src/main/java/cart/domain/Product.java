package cart.domain;

public class Product {
    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private Double pointRatio;
    private Boolean pointAvailable;
    
    public Product(final String name, final Long price, final String imageUrl, final Double pointRatio, final Boolean pointAvailable) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
    }
    
    public Product(final Long id, final String name, final Long price, final String imageUrl, final Double pointRatio, final Boolean pointAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
    }
    
    public Long calculatePointToAdd() {
        return Math.round(price * (pointRatio / 100.0));
    }
    
    public Long calculateAvailablePoint() {
        if (pointAvailable) {
            return price;
        }
        
        return 0L;
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
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", pointRatio=" + pointRatio +
                ", pointAvailable=" + pointAvailable +
                '}';
    }
}
