package cart.entity.order;

import java.util.Date;

public class OrderTableEntity {

    private final Long id;
    private final Long memberId;
    private final Date createAt;
    private final int deliveryFee;

    public OrderTableEntity(final Long id, final Long memberId, final Date createAt, final int deliveryFee) {
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

    public Date getCreateAt() {
        return createAt;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
