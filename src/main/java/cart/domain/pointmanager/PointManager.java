package cart.domain.pointmanager;

public interface PointManager {

    int calculateEarnedPoints(final int totalPrice);

    int calculateLimitPoints(final int totalPrice);

    int calculateAvailablePoints(final int currentPoints, int limitPoints);
}
