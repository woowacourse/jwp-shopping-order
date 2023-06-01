package cart.entity;

import cart.domain.Member;
import cart.domain.Order;
import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final int earnedPoints;
    private final int usedPoints;
    private final int totalPrice;
    private final int payPrice;
    private final LocalDateTime orderDate;

    public OrderEntity(
            Long id,
            Long memberId,
            int earnedPoints,
            int usedPoints,
            int totalPrice,
            int payPrice,
            LocalDateTime orderDate) {
        this.id = id;
        this.memberId = memberId;
        this.earnedPoints = earnedPoints;
        this.usedPoints = usedPoints;
        this.totalPrice = totalPrice;
        this.payPrice = payPrice;
        this.orderDate = orderDate;
    }

    public OrderEntity(
            Long memberId,
            int earnedPoints,
            int usedPoints,
            int totalPrice,
            int payPrice,
            LocalDateTime orderDate) {
        this(null, memberId, earnedPoints, usedPoints, totalPrice, payPrice, orderDate);
    }

    public static OrderEntity of(Member member, Order order) {
        return new OrderEntity(
                member.getId(),
                order.getEarnedPoints().getValue(),
                order.getUsedPoints().getValue(),
                order.calculateTotalPrice().getValue(),
                order.calculatePayPrice().getValue(),
                order.getOrderDate()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPayPrice() {
        return payPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}
