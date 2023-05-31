package cart.entity;

import java.util.Date;
import java.util.Objects;

public class OrderEntity {

    private Long id;
    private Long memberId;
    private int orderStatusId;
    private Date createAt;

    public OrderEntity(Long id, Long memberId, int orderStatusId, Date createAt) {
        this.id = id;
        this.memberId = memberId;
        this.orderStatusId = orderStatusId;
        this.createAt = createAt;
    }

    public OrderEntity(Long memberId, int orderStatusId) {
        this.memberId = memberId;
        this.orderStatusId = orderStatusId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(memberId, that.memberId) && Objects.equals(orderStatusId, that.orderStatusId) && Objects.equals(createAt, that.createAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, orderStatusId, createAt);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", orderStatusId=" + orderStatusId +
                ", createAt=" + createAt +
                '}';
    }
}
