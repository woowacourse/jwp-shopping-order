package cart.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    private final Long id;
    private final Member member;
    private final LocalDateTime orderedAt;
    private final Point usedPoint;
    private final OrderedItems orderedItems;

    private Point savedPoint;

    public Order(Member member, LocalDateTime orderedAt, Point usedPoint) {
        this.id = null;
        this.member = member;
        this.orderedAt = orderedAt;
        this.usedPoint = usedPoint;
        this.orderedItems = new OrderedItems(new ArrayList<>());
    }

    public Order(Long id, Member member, LocalDateTime orderedAt, Point usedPoint, OrderedItems orderedItems) {
        this.id = id;
        this.member = member;
        this.orderedAt = orderedAt;
        this.usedPoint = usedPoint;
        this.orderedItems = orderedItems;
    }

    public void calculateSavedPoint() {
        Integer totalPrice = orderedItems.calculateTotalPrice();
        this.savedPoint = PointEarningPolicy.calculateSavingPoints(totalPrice - usedPoint.getValue());
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public Point getUsedPoint() {
        return usedPoint;
    }

    public Point getSavedPoint() {
        return savedPoint;
    }

    public OrderedItems getOrderedItems() {
        return orderedItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", orderedAt=" + orderedAt +
                ", usedPoint=" + usedPoint +
                ", orderedItems=" + orderedItems +
                ", savedPoint=" + savedPoint +
                '}';
    }
}
