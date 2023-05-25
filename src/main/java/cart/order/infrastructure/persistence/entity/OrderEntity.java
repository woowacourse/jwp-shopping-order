package cart.order.infrastructure.persistence.entity;

public class OrderEntity {

    private final Long id;
    private final Long memberId;

    public OrderEntity(Long id, Long memberId) {
        this.id = id;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }
}
