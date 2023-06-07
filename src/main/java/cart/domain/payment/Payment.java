package cart.domain.payment;

import cart.domain.vo.Cash;
import cart.domain.vo.DeliveryFee;
import cart.domain.vo.Point;
import cart.domain.vo.Price;

import java.util.Objects;

public class Payment {

    private final int totalPrice;
    private final Point usedPoint;
    private final Cash userPayment;

    private Payment(int totalPrice, Point usedPoint, Cash userPayment) {
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.userPayment = userPayment;
    }

    public static Payment of(Price totalPriceOfAllProducts, DeliveryFee deliveryFee, Point usedPoint) {
        int totalPrice = totalPriceOfAllProducts.getPrice() + deliveryFee.getDeliveryFee();
        Cash cash = new Cash(totalPrice - usedPoint.getPoint());
        return new Payment(totalPrice, usedPoint, cash);
    }

    public static Payment of(int totalPayment, int usedPoint) {
        return new Payment(totalPayment, new Point(usedPoint), new Cash(totalPayment - usedPoint));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return totalPrice == payment.totalPrice && Objects.equals(usedPoint, payment.usedPoint) && Objects.equals(userPayment, payment.userPayment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPrice, usedPoint, userPayment);
    }

    public Point getUsedPoint() {
        return usedPoint;
    }

    public Cash getUserPayment() {
        return userPayment;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
