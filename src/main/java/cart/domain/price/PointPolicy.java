package cart.domain.price;

public interface PointPolicy {
    Point calculate(Price price);
}
