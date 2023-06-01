package cart.domain;

public class Payment {

    private final int totalPayment;
    private final int usedPoint;
    private final int userPayment;

    public Payment(int totalPayment, int usedPoint) {
        this.totalPayment = totalPayment;
        this.usedPoint = usedPoint;
        this.userPayment = totalPayment - usedPoint;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getUserPayment() {
        return userPayment;
    }
}
