package cart.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;
    private final OrderStatus orderStatus;
    private final Points usedPoint;
    private final List<OrderItem> orderItems;
    private final LocalDate orderAt;

    public Order(Points usedPoints, List<OrderItem> orderItems) {
        this.orderStatus = OrderStatus.PENDING;
        this.usedPoint = usedPoints;
        this.orderItems = orderItems;
        this.orderAt = LocalDate.now();
    }

    public Order(Long id, OrderStatus orderStatus, Points usedPoint, List<OrderItem> orderItems, LocalDate orderAt) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.usedPoint = usedPoint;
        this.orderItems = orderItems;
        this.orderAt = orderAt;
    }

    public Point calculateSavedPoint(PointAccumulationPolicy pointAccumulationPolicy) {
        int totalCost = orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum() - usedPoint.getTotalPoint();

        return pointAccumulationPolicy.calculateAccumulationPoint(totalCost);
    }

    public int getTotalPayAmount() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum() - getTotalUsedPoint();
    }

    public String getFirstItemName() {
        try {
            return orderItems.get(0).getProductName();
        } catch (IndexOutOfBoundsException exception) {
            throw new IllegalArgumentException();
        }
    }

    public String getFirstItemImageUrl() {
        try {
            return orderItems.get(0).getProductImageUrl();
        } catch (IndexOutOfBoundsException exception) {
            throw new IllegalArgumentException();
        }
    }

    public int getItemSize() {
        return orderItems.size();
    }
    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<Point> getUsedPoints() {
        return usedPoint.getPoints();
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public LocalDate getOrderAt() {
        return orderAt;
    }

    public int getTotalUsedPoint() {
        return usedPoint.getTotalPoint();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && orderStatus == order.orderStatus && Objects.equals(usedPoint, order.usedPoint) && Objects.equals(orderItems, order.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderStatus, usedPoint, orderItems);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", usedPoint=" + usedPoint +
                ", orderItems=" + orderItems +
                '}';
    }
}
