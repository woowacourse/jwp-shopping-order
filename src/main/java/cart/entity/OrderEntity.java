package cart.entity;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final int deliveryFee;

    public OrderEntity(Long memberId, int deliveryFee) {
        this(null, memberId, deliveryFee);
    }

    public OrderEntity(Long id, Long memberId, int deliveryFee) {
        this.id = id;
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
