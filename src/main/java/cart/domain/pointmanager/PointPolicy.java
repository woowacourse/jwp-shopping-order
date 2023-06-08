package cart.domain.pointmanager;

public interface PointPolicy {

    int calculateEarnedPoints(final int totalPrice);

    int calculateLimitPoints(final int totalPrice);

    int calculateAvailablePoints(final int currentPoints, int limitPoints);
}
