package cart.domain;

import cart.exception.OrderException;
import cart.exception.PointException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private final Long id;
    private final List<OrderItem> orderItems;
    private final Long memberId;
    private final Money usedPoints;
    private final Money earnedPoints;
    private final LocalDateTime orderDate;

    private Order(List<OrderItem> orderItems, Long memberId, Money usedPoints, Money earnedPoints,
            LocalDateTime orderDate) {
        this(null, orderItems, memberId, usedPoints, earnedPoints, orderDate);
    }

    private Order(Long id, List<OrderItem> orderItems, Long memberId, Money usedPoints, Money earnedPoints,
            LocalDateTime orderDate) {
        this.id = id;
        this.orderItems = orderItems;
        this.memberId = memberId;
        this.usedPoints = usedPoints;
        this.earnedPoints = earnedPoints;
        this.orderDate = orderDate;
    }

    public static Order of(List<OrderItem> orderItems, Long memberId, Money usedPoints,
            PointDiscountPolicy pointDiscountPolicy, PointEarnPolicy pointEarnPolicy) {
        Money totalPrice = calculateTotalPrice(orderItems);

        validateUsedPoints(totalPrice, usedPoints, pointDiscountPolicy);

        Money earnedPoints = pointEarnPolicy.calculateEarnPoints(totalPrice);

        return new Order(orderItems, memberId, usedPoints, earnedPoints, LocalDateTime.now());
    }

    public static Order of(Long id, List<OrderItem> orderItems, Long memberId, Money usedPoints, Money earnedPoints,
            LocalDateTime orderDate) {
        return new Order(id, orderItems, memberId, usedPoints, earnedPoints, orderDate);
    }

    private static Money calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItem::calculateTotalPrice).reduce(new Money(0), Money::add);
    }

    private static void validateUsedPoints(Money totalPrice, Money usedPoints,
            PointDiscountPolicy pointDiscountPolicy) {
        if (pointDiscountPolicy.isUnAvailableUsedPoints(totalPrice, usedPoints)) {
            throw new PointException.InvalidPolicy();
        }
    }

    public Money calculateTotalPrice() {
        return orderItems.stream().map(OrderItem::calculateTotalPrice).reduce(new Money(0), Money::add);
    }

    public Money calculatePayPrice() {
        return calculateTotalPrice().subtract(usedPoints);
    }

    public void checkOwner(Member member) {
        if(!member.getId().equals(memberId)) {
            throw new OrderException.IllegalMember();
        }
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    public Money getEarnedPoints() {
        return earnedPoints;
    }

    public Money getUsedPoints() {
        return usedPoints;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}
