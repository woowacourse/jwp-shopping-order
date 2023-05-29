package cart.domain.orderproduct;

import cart.domain.member.Member;
import cart.domain.member.MemberPoint;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private Long id;
    private final Member member;
    private final MemberPoint usedPoint;
//    private final ProductPrice totalPrice;
    private LocalDateTime createdAt;

    public Order(final Member member, final MemberPoint usedPoint) {
        this.member = member;
        this.usedPoint = usedPoint;
    }

    public Order(final Long id, final Member member, final MemberPoint usedPoint, final LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
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
                ", createdAt=" + createdAt +
                '}';
    }
}
