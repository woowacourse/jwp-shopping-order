package cart.dao.dto;

import java.time.LocalDateTime;

import cart.domain.Order;

public class OrderDto {

    private Long id;
    private Long memberId;
    private LocalDateTime createdAt;

    public OrderDto(Long id, Long memberId, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.createdAt = createdAt;
    }

    public static OrderDto of(Order order) {
        return new OrderDto(
                order.getId(),
                order.getOwner().getId(),
                order.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
