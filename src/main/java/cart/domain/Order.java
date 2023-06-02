package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Member member;
    private List<OrderItem> orderItems;
    private LocalDateTime orderDateTime;

    public Order(Member member, List<OrderItem> orderItems) {
        this.member = member;
        this.orderItems = orderItems;
    }

    public Order(Long id, Member member, LocalDateTime orderDateTime) {
        this.id = id;
        this.member = member;
        this.orderDateTime = orderDateTime;
    }

    public Order(Long id, Member member, List<OrderItem> orderItems, LocalDateTime orderDateTime) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.orderDateTime = orderDateTime;
    }

    public Payment calculatePayment(Point usePoint) {
        int totalProductPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalProductPrice += orderItem.calculatePrice();
        }
        int deliveryFee = DeliveryPolicy.calculateDeliveryFee(totalProductPrice);
        PointPolicy.usePoint(member, usePoint);
        PointPolicy.earnPoint(member, totalProductPrice);
        int totalPrice = totalProductPrice + deliveryFee - usePoint.getValue();

        return new Payment(totalProductPrice, deliveryFee, usePoint.getValue(), totalPrice);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", orderItems=" + orderItems +
                '}';
    }
}
