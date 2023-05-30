package cart.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Orders {

    private Long id;
    private final Member member;
    private final Point point;
    private int earnedPoint;
    private int usedPoint;
    private LocalDateTime createdAt;

    public Orders(Member member, Point point, int earnedPoint, int usedPoint, LocalDateTime createdAt) {
        this.member = member;
        this.point = point;
        this.earnedPoint = earnedPoint;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    public Orders(Long id, Member member, Point point, int earnedPoint, int usedPoint, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.point = point;
        this.earnedPoint = earnedPoint;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Point getPoint() {
        return point;
    }

    public int getEarnedPoint() {
        return earnedPoint;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orders)) return false;
        Orders orders = (Orders) o;
        return Objects.equals(id, orders.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
