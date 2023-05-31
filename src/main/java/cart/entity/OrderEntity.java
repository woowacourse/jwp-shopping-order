package cart.entity;

public class OrderEntity {

    private Long id;
    private Long memberId;

    public OrderEntity(Long memberId) {
        this(null, memberId);
    }

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
