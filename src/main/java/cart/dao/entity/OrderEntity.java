package cart.dao.entity;

import java.sql.Timestamp;

public class OrderEntity {

    private Long id;
    private long memberId;
    private Timestamp createdAt;

    public OrderEntity(long memberId) {
        this(null, memberId);
    }

    public OrderEntity(Long id, long memberId) {
        this(id, memberId, null);
    }

    public OrderEntity(Long id, long memberId, Timestamp createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
