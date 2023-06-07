package cart.productpoint.domain;

import java.util.Objects;

public class ProductPoint {
    private Long id;
    private final Double pointRatio;
    private final boolean pointAvailable;
    
    public ProductPoint(final Double pointRatio, final boolean pointAvailable) {
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
    }
    
    public ProductPoint(final Long id, final Double pointRatio, final boolean pointAvailable) {
        this.id = id;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
    }
    
    public Long calculatePointToAdd(final Long productPrice) {
        return Math.round(productPrice * (pointRatio / 100.0));
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductPoint productPoint = (ProductPoint) o;
        return pointAvailable == productPoint.pointAvailable && Objects.equals(pointRatio, productPoint.pointRatio);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(pointRatio, pointAvailable);
    }
    
    @Override
    public String toString() {
        return "Point{" +
                "pointRatio=" + pointRatio +
                ", pointAvailable=" + pointAvailable +
                '}';
    }
}
