package cart.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Order {

    public static final double POINT_CHARGE_RATE = 0.1;
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

    public static Order of(final Member member, final int usedPoints, final List<CartItem> cartItems) {
        validateCartItems(cartItems);
        validatePoint(member.getPoints(), usedPoints);

        final List<OrderItem> orderItems = cartItems.stream().map(OrderItem::of).collect(Collectors.toList());
        final int totalPrice = calculateTotalPrice(orderItems);
        final int payPrice = totalPrice - usedPoints;
        final int earnedPoints = (int) calculateEarnedPoints(totalPrice);

        return new Order(member.getId(), orderItems, totalPrice, payPrice, earnedPoints, usedPoints);
    }

    private static void validateCartItems(final List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("최소 하나 이상의 상품이 포함되어야 합니다.");
        }
    }

    private static void validatePoint(final int memberPoint, final int usedPoints) {
        if (memberPoint < usedPoints) {
            throw new IllegalArgumentException("보유한 포인트가 부족합니다.");
        }
    }

    private static int calculateTotalPrice(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .mapToInt(i -> i)
                .sum();
    }

    private static double calculateEarnedPoints(final int totalPrice) {
        return totalPrice * POINT_CHARGE_RATE;
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
