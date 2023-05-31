package cart.domain;

import java.util.List;

public class Order {
    private Member member;
    private List<OrderItem> orderItems;

    public Order(Member member, List<OrderItem> orderItems) {
        this.member = member;
        this.orderItems = orderItems;
    }

    public Payment calculatePayment(Point usePoint) {
        int totalProductPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalProductPrice += orderItem.calculatePrice();
        }
        int deliveryFee = DeliveryPolicy.calculateDeliveryFee(totalProductPrice);
        PointPolicy.usePoint(member, usePoint);
        int totalPrice = totalProductPrice + deliveryFee - usePoint.getValue();

        return new Payment(totalProductPrice, deliveryFee, usePoint.getValue(), totalPrice);
    }
}
