package cart.domain;

import cart.exception.illegalexception.IllegalOrderException;
import cart.exception.authexception.OrderUnauthorizedException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;
    private final List<OrderItem> items;
    private final Timestamp createdAt;
    private final Member member;

    public Order(List<OrderItem> items, Member member) {
        this(items, null, member);
    }

    public Order(Long id, List<OrderItem> items, Member member) {
        this(id, items, null, member);
    }

    public Order(List<OrderItem> items, Timestamp createdAt, Member member) {
        this(null, items, createdAt, member);
    }

    public Order(Long id, List<OrderItem> items, Timestamp createdAt, Member member) {
        validate(items);
        this.id = id;
        this.items = items;
        this.createdAt = createdAt;
        this.member = member;
    }

    private void validate(List<OrderItem> items) {
        if (items.isEmpty()) {
            throw new IllegalOrderException("주문 상품은 비어있을 수 없습니다.");
        }
    }

    public Money calculateTotalPrice() {
        return items.stream()
            .map(OrderItem::calculatePrice)
            .reduce(Money.MIN, Money::add);
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new OrderUnauthorizedException(id);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        if (order.id == null && id == null) {
            return false;
        }
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public long getMemberId() {
        return member.getId();
    }

    public Member getMember() {
        return member;
    }
}
