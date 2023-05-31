package cart.domain;

import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;
    private final OrderStatus orderStatus;
    private final int savedPoint;
    private final int usedPoint;
    private final List<OrderItem> orderItems;
    private final Member member;

    public Order(int usedPoint, List<OrderItem> orderItems, Member member) {
        this.savedPoint = calculateSavedPoint(orderItems);
        this.orderStatus = OrderStatus.PENDING;
        this.usedPoint = usedPoint;
        this.orderItems = orderItems;
        this.member = member;
    }

    public Order(Long id, OrderStatus orderStatus, int savedPoint, int usedPoint, List<OrderItem> orderItems, Member member) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.savedPoint = savedPoint;
        this.usedPoint = usedPoint;
        this.orderItems = orderItems;
        this.member = member;
    }

    private static int calculateSavedPoint(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();  // 포인트 정책 반영
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public int getSavedPoint() {
        return savedPoint;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return savedPoint == order.savedPoint && usedPoint == order.usedPoint && Objects.equals(id, order.id) && orderStatus == order.orderStatus && Objects.equals(orderItems, order.orderItems) && Objects.equals(member, order.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderStatus, savedPoint, usedPoint, orderItems, member);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", savedPoint=" + savedPoint +
                ", usedPoint=" + usedPoint +
                ", orderItems=" + orderItems +
                ", member=" + member +
                '}';
    }
}
