package cart.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final Integer usedMoney;
    private final Integer usedPoint;
    private final Timestamp createdAt;

    public OrderEntity(Long id, Long memberId, Integer usedMoney, Integer usedPoint, Timestamp createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.usedMoney = usedMoney;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    public OrderEntity(Long memberId, Integer usedMoney, Integer usedPoint) {
        this(null, memberId, usedMoney, usedPoint, Timestamp.valueOf(LocalDateTime.now()));
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Integer getUsedMoney() {
        return usedMoney;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
