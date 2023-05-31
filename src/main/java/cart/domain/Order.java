package cart.domain;

import java.util.List;

public class Order {

    private final Long id;
    private final List<OrderItem> orderItems;
    private final int totalPrice;
    private final int payPrice;
    private final int earnedPoints;
    private final int usedPoints;
    private final String orderDate;

    public Order(final Long id, final List<OrderItem> orderItems, final int totalPrice, final int payPrice, final int earnedPoints, final int usedPoints, final String orderDate) {
        this.id = id;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.payPrice = payPrice;
        this.earnedPoints = earnedPoints;
        this.usedPoints = usedPoints;
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPayPrice() {
        return payPrice;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
