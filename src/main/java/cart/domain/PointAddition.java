package cart.domain;

import java.time.LocalDateTime;

public class PointAddition {

    private final Long id;
    private final Long memberId;
    private final Long orderId;
    private final Integer amount;
    private final LocalDateTime createdAt;
    private final LocalDateTime expireAt;

    public PointAddition(Long id, Long memberId, Long orderId, Integer amount, LocalDateTime createdAt,
        LocalDateTime expireAt) {
        this.id = id;
        this.memberId = memberId;
        this.orderId = orderId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.expireAt = expireAt;
    }

    public static PointAddition from (Long memberId, Long orderId, int amount, LocalDateTime createdAt, LocalDateTime expireAt) {
        return new PointAddition(null, memberId, orderId, amount, createdAt, expireAt);
    }

    public PointAddition reduce(int amount) {
        return new PointAddition(id, memberId, orderId, this.amount - amount, createdAt, expireAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Integer getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }
}
