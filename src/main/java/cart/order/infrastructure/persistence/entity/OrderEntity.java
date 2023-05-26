package cart.order.infrastructure.persistence.entity;

public class OrderEntity {

    private Long id;
    private Long memberId;

    OrderEntity() {
    }

    public OrderEntity(Long id, Long memberId) {
        this.id = id;
        this.memberId = memberId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }
}
