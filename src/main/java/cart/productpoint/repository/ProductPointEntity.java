package cart.productpoint.repository;

import cart.productpoint.domain.ProductPoint;

public class ProductPointEntity {
    private final Long id;
    private final Double pointRatio;
    private final boolean pointAvailable;
    
    public ProductPointEntity(final Long id, final Double pointRatio, final boolean pointAvailable) {
        this.id = id;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
    }
    
    public static ProductPointEntity from(final ProductPoint productPoint) {
        return new ProductPointEntity(productPoint.getId(), productPoint.getPointRatio(), productPoint.isPointAvailable());
    }
    
    public Long getId() {
        return id;
    }
    
    public Double getPointRatio() {
        return pointRatio;
    }
    
    public boolean isPointAvailable() {
        return pointAvailable;
    }
    
    @Override
    public String toString() {
        return "PointEntity{" +
                "id=" + id +
                ", pointRatio=" + pointRatio +
                ", pointAvailable=" + pointAvailable +
                '}';
    }
}
