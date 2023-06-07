package cart.product.domain;

import cart.productpoint.domain.ProductPoint;
import cart.productpoint.repository.ProductPointEntity;
import cart.product.repository.ProductEntity;

import java.util.Objects;

public class Product {
    private Long id;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private final ProductPoint productPoint;
    
    public Product(final String name, final Long price, final String imageUrl, final Double pointRatio, final Boolean pointAvailable) {
        this(null, name, price, imageUrl, new ProductPoint(pointRatio, pointAvailable));
    }
    
    public Product(final Long id, final String name, final Long price, final String imageUrl, final Double pointRatio, final Boolean pointAvailable) {
        this(id, name, price, imageUrl, new ProductPoint(pointRatio, pointAvailable));
    }
    
    public Product(final Long id, final String name, final Long price, final String imageUrl, final ProductPoint productPoint) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.productPoint = productPoint;
    }
    
    public static Product of(final ProductEntity productEntity, final ProductPointEntity productPointEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                new ProductPoint(productPointEntity.getId(), productPointEntity.getPointRatio(), productPointEntity.isPointAvailable())
        );
    }
    
    public Long calculatePointToAdd() {
        return productPoint.calculatePointToAdd(price);
    }
    
    public Long calculateAvailablePoint() {
        if (productPoint.isPointAvailable()) {
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
    
    public ProductPoint getPoint() {
        return productPoint;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(imageUrl, product.imageUrl) && Objects.equals(productPoint, product.productPoint);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, imageUrl, productPoint);
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", point=" + productPoint +
                '}';
    }
}
