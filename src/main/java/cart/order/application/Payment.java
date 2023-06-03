package cart.order.application;

import cart.member.application.Member;
import cart.member.application.Point;
import cart.member.application.PointPolicy;

public class Payment {
    private final int totalProductPrice;
    private final int totalDeliveryFee;
    private final int usePoint;
    private final int totalPrice;

    public Payment(int totalProductPrice, int totalDeliveryFee, int usePoint, int totalPrice) {
        this.totalProductPrice = totalProductPrice;
        this.totalDeliveryFee = totalDeliveryFee;
        this.usePoint = usePoint;
        this.totalPrice = totalPrice;
    }

    public static Payment makePayment(Order order, Member member, Point usePoint) {
        int totalProductPrice = order.calculateTotalProductPrice();

        int deliveryFee = DeliveryPolicy.calculateDeliveryFee(totalProductPrice);

        PointPolicy.usePoint(member, usePoint);
        PointPolicy.earnPoint(member, totalProductPrice);

        int totalPrice = totalProductPrice + deliveryFee - usePoint.getValue();

        return new Payment(totalProductPrice, deliveryFee, usePoint.getValue(), totalPrice);
    }

    public int getTotalProductPrice() {
        return totalProductPrice;
    }

    public int getTotalDeliveryFee() {
        return totalDeliveryFee;
    }

    public int getUsePoint() {
        return usePoint;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
