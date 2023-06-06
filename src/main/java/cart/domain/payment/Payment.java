package cart.domain.payment;

public class Payment {

    private final int totalPrice;
    private final int usedPoint;
    private final int userPayment;

    private Payment(int totalPrice, int userPayment, int usedPoint) {
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.userPayment = userPayment;
    }

    public static Payment of(int productTotalPrice, int deliveryFee, int usedPoint) {
        int totalPrice = productTotalPrice + deliveryFee;
        int userPayment = totalPrice - usedPoint;
        return new Payment(totalPrice, userPayment, usedPoint);
    }

    public static Payment of(int userPayment, int usedPoint) {
        int totalPrice = userPayment + usedPoint;
        return new Payment(totalPrice, userPayment, usedPoint);
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
