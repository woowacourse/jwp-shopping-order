package cart.domain;

public class PointCalculator {

    public int calculatePoint(int payAmount) {
        if (payAmount < 50_000) {
            return payAmount * 5 / 100;
        }
        if (payAmount < 100_000) {
            return payAmount * 8 / 100;
        }
        return payAmount * 10 / 100;
    }
}
