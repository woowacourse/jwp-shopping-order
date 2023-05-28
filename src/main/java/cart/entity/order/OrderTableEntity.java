package cart.entity.order;

public class OrderTableEntity {

    private final Long id;
    private final Long memberId;
    private final String createAt;
    private final int deliveryFee;

    public OrderTableEntity(final Long id, final Long memberId, final String createAt, final int deliveryFee) {
        this.id = id;
        this.memberId = memberId;
        this.createAt = createAt;
        this.deliveryFee = deliveryFee;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
