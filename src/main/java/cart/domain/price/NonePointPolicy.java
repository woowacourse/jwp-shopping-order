package cart.domain.price;

public class NonePointPolicy implements PointPolicy {
    @Override
    public Point calculate(Price price) {
        return Point.ZERO;
    }
}
