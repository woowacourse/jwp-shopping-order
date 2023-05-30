package cart.dao.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderEntity {

    private final Long id;
    private final MemberEntity memberEntity;
    private final Integer usedPoint;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public OrderEntity(MemberEntity memberEntity, Integer usedPoint) {
        this(null, memberEntity, usedPoint, null, null);
    }

    public OrderEntity(Long id, MemberEntity memberEntity, Integer usedPoint) {
        this(id, memberEntity, usedPoint, null, null);
    }

    public OrderEntity(Long id, MemberEntity memberEntity, Integer usedPoint, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberEntity = memberEntity;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public OrderEntity assignId(Long id) {
        return new OrderEntity(id, memberEntity, usedPoint, createdAt, updatedAt);
    }

    public Long getId() {
        return id;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public Long getMemberId() {
        return memberEntity.getId();
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
