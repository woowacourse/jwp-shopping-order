package cart.product.domain;

import cart.product.repository.ProductEntity;

import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private Double pointRatio;
    private boolean pointAvailable;
    
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
    
    public static Product from(final ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                productEntity.getPointRatio(),
                productEntity.isPointAvailable()
        );
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
    
    public boolean getPointAvailable() {
        return pointAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return pointAvailable == product.pointAvailable && Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(imageUrl, product.imageUrl) && Objects.equals(pointRatio, product.pointRatio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, imageUrl, pointRatio, pointAvailable);
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
