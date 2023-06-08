package cart.domain;

public interface PointAccumulationPolicy {

    Point calculateAccumulationPoint(int totalCost);
}
