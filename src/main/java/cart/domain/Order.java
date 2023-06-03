package cart.domain;

import cart.exception.auth.AuthorizationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long id;
    private Member member;
    private List<OrderItem> orderItems;
    private LocalDateTime orderDateTime;

    public Order(Member member, List<OrderItem> orderItems) {
        this.member = member;
        this.orderItems = orderItems;
    }

    public Order(Long id, Member member, LocalDateTime orderDateTime) {
        this.id = id;
        this.member = member;
        this.orderDateTime = orderDateTime;
    }

    public Order(Long id, Member member, List<OrderItem> orderItems, LocalDateTime orderDateTime) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.orderDateTime = orderDateTime;
    }

    public static Order makeOrder(Member member, List<OrderItem> orderItems) {
        orderItems.forEach(OrderItem::execute);
        return new Order(member, orderItems);
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new AuthorizationException("해당 member의 order이 아닙니다.");
        }
    }

    public int calculateTotalProductPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::calculatePrice)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", orderItems=" + orderItems +
                '}';
    }
}
