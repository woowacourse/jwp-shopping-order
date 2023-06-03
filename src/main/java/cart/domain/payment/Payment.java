package cart.domain.payment;

public class Payment {

    private final int totalPrice;
    private final int usedPoint;
    private final int userPayment;

    public Payment(int totalPrice, int usedPoint) {
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.userPayment = totalPrice - usedPoint;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getUserPayment() {
        return userPayment;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "totalPrice=" + totalPrice +
                ", usedPoint=" + usedPoint +
                ", userPayment=" + userPayment +
                '}';
    }
}
