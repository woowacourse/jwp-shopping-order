package cart.domain.orderproduct;

import cart.domain.member.Member;
import cart.domain.member.MemberPoint;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private Long id;
    private final Member member;
    private final MemberPoint usedPoint;
    private LocalDateTime orderedAt;

    public Order(final Member member, final MemberPoint usedPoint) {
        this.member = member;
        this.usedPoint = usedPoint;
    }

    public Order(final Long id, final Member member, final MemberPoint usedPoint, final LocalDateTime orderedAt) {
        this.id = id;
        this.member = member;
        this.usedPoint = usedPoint;
        this.orderedAt = orderedAt;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public MemberPoint getUsedPoint() {
        return usedPoint;
    }

    public Integer getUsedPointValue() {
        return usedPoint.getPoint();
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", usedPoint=" + usedPoint +
                ", createdAt=" + orderedAt +
                '}';
    }
}
