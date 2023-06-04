package cart.domain.price;

public class AdditionalPointPolicy implements PointPolicy {
    private final PointPolicy pointPolicy;
    private final PointPolicy nextPolicy;

    public AdditionalPointPolicy(PointPolicy pointPolicy, PointPolicy nextPolicy) {
        this.pointPolicy = pointPolicy;
        this.nextPolicy = nextPolicy;
    }

    @Override
    public Point calculate(Price price) {
        return pointPolicy.calculate(price).plus(nextPolicy.calculate(price));
    }
}
