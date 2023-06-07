package cart.product.repository;

public class ProductEntity {
    private final Long id;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private final Long productPointId;
    
    public ProductEntity(final Long id, final String name, final Long price, final String imageUrl, final Long productPointId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.productPointId = productPointId;
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
    
    public Long getProductPointId() {
        return productPointId;
    }
    
    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", productPointId=" + productPointId +
                '}';
    }
}
