package cart.dao.dto.order;

import java.sql.Timestamp;

public class OrderDto {

    private final Long id;
    private final long memberId;
    private final Timestamp createdAt;

    public OrderDto(long memberId) {
        this(null, memberId);
    }

    public OrderDto(Long id, long memberId) {
        this(id, memberId, null);
    }

    public OrderDto(Long id, long memberId, Timestamp createdAt) {
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
