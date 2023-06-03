package cart.repository;

import java.time.LocalDateTime;

import cart.domain.Order;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final LocalDateTime orderAt;
    private final int payAmount;
    private final String orderStatus;

    public OrderEntity(Long id, Long memberId, LocalDateTime orderAt, int payAmount, String orderStatus) {
        this.id = id;
        this.memberId = memberId;
        this.orderAt = orderAt;
        this.payAmount = payAmount;
        this.orderStatus = orderStatus;
    }

    public OrderEntity(Long memberId, LocalDateTime orderAt, int payAmount, String orderStatus) {
        this(null, memberId, orderAt, payAmount, orderStatus);
    }

    public static OrderEntity of(Order order) {
        return new OrderEntity(order.getMember().getId(), order.getOrderAt(), order.getPayAmount(),
            order.getOrderStatus().getDisplayName());
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
