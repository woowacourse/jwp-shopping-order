package cart.domain;

import cart.domain.pointmanager.PointManager;
import cart.exception.OrderException;
import cart.exception.PointException;

import java.util.List;
import java.util.stream.Collectors;

public class Order {
    
    private final Long id;
    private final Long memberId;
    private final List<OrderItem> orderItems;
    private final int totalPrice;
    private final int payPrice;
    private final int earnedPoints;
    private final int usedPoints;
    private final String orderDate;

    private Order(final Long memberId, final List<OrderItem> orderItems, final int totalPrice, final int payPrice, final int earnedPoints, final int usedPoints) {
        this(null, memberId, orderItems, totalPrice, payPrice, earnedPoints, usedPoints, null);
    }

    public Order(final Long id, final Long memberId, final List<OrderItem> orderItems, final int totalPrice, final int payPrice, final int earnedPoints, final int usedPoints, final String orderDate) {
        this.id = id;
        this.memberId = memberId;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.payPrice = payPrice;
        this.earnedPoints = earnedPoints;
        this.usedPoints = usedPoints;
        this.orderDate = orderDate;
    }

    public static Order of(final Member member, final int usedPoints, final List<CartItem> cartItems, final PointManager pointManager) {
        validateCartItems(cartItems);

        final List<OrderItem> orderItems = cartItems.stream().map(OrderItem::of).collect(Collectors.toList());
        final int totalPrice = calculateTotalPrice(orderItems);

        validatePoint(totalPrice, member.getPoints(), usedPoints, pointManager);

        final int payPrice = totalPrice - usedPoints;
        final int earnedPoints = pointManager.calculateEarnedPoints(totalPrice);

        return new Order(member.getId(), orderItems, totalPrice, payPrice, earnedPoints, usedPoints);
    }

    private static void validateCartItems(final List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            throw new OrderException.EmptyOrder();
        }
    }

    private static int calculateTotalPrice(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .mapToInt(i -> i)
                .sum();
    }

    private static void validatePoint(final int totalPrice, final int memberPoint, final int usedPoints, final PointManager pointManager) {
        final int limitPoints = pointManager.calculateLimitPoints(totalPrice);
        if (limitPoints < usedPoints) {
            throw new PointException.BiggerThenLimit(limitPoints, usedPoints);
        }
        if (memberPoint < usedPoints) {
            throw new PointException.NotEnough(memberPoint, usedPoints);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
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
