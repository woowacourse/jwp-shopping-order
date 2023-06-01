package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final Long id;
    private final Member member;
    private final List<OrderDetail> orderDetails;
    private final LocalDateTime createdAt;

    public Order(Long id, Member member, List<OrderDetail> orderDetails, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.orderDetails = orderDetails;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
