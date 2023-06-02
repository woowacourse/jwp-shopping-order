package cart.domain.order;

import cart.domain.member.Member;
import cart.domain.vo.Money;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderHistory {

    private final Long id;
    private final Member member;
    private final OrderItems orderItems;
    private final Money totalPrice;
    private final Money usePoint;
    private final LocalDateTime createdAt;

    public OrderHistory(Long id, Member member, OrderItems orderItems, Money totalPrice, Money usePoint, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.usePoint = usePoint;
        this.createdAt = createdAt;
    }

    public boolean isNotOwner(Member otherMember) {
        return member.isNotSame(otherMember);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public Money getTotalPrice() {
        return totalPrice;
    }

    public Money getUsePoint() {
        return usePoint;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderHistory that = (OrderHistory) o;
        return Objects.equals(member, that.member) && Objects.equals(orderItems, that.orderItems) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(usePoint, that.usePoint) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, orderItems, totalPrice, usePoint, createdAt);
    }

    @Override
    public String toString() {
        return "OrderHistory{" +
                "id=" + id +
                ", member=" + member +
                ", orderItems=" + orderItems +
                ", totalPrice=" + totalPrice +
                ", usePoint=" + usePoint +
                ", createdAt=" + createdAt +
                '}';
    }
}
