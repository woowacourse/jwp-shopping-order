package cart.domain;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Member member;
    private LocalDateTime orderedAt;
    private Point usedPoint;
    private OrderedItems orderedItems;

    public Order(Member member, LocalDateTime orderedAt, Point usedPoint) {
        this.member = member;
        this.orderedAt = orderedAt;
        this.usedPoint = usedPoint;
    }

    public Order(Long id, Member member, LocalDateTime orderedAt, Point usedPoint, OrderedItems orderedItems) {
        this.id = id;
        this.member = member;
        this.orderedAt = orderedAt;
        this.usedPoint = usedPoint;
        this.orderedItems = orderedItems;
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

    public OrderedItems getOrderedItems() {
        return orderedItems;
    }
}
