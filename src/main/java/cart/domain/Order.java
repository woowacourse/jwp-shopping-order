package cart.domain;

import cart.exception.OrderException;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new OrderException.IllegalMember(this, member);
        }
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
