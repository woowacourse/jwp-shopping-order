package cart.domain.pointmanager;

public final class DefaultPointManager implements PointManager {

    private static final double POINT_CHARGE_RATE = 0.1;
    private static final double POINT_SPEND_RATE = 0.1;


    @Override
    public int calculateEarnedPoints(final int totalPrice) {
        return (int) (totalPrice * POINT_CHARGE_RATE);
    }

    @Override
    public int calculateLimitPoints(final int totalPrice) {
        return (int) (totalPrice * POINT_SPEND_RATE);
    }

    @Override
    public int calculateAvailablePoints(final int currentPoints, final int limitPoints) {
        return Math.min(currentPoints, limitPoints);
    }


}
