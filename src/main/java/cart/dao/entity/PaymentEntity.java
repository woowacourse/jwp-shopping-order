package cart.dao.entity;

import cart.domain.order.payment.Payment;

public class PaymentEntity {

    private final Long id;
    private final Long orderId;
    private final int originalTotalPrice; // TODO: 2023/06/08 이거 빼도 되는지 확인하기

    public PaymentEntity(final Long id, final Long orderId, final int originalTotalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.originalTotalPrice = originalTotalPrice;
    }

    public static PaymentEntity from(final Payment payment) {
        return new PaymentEntity(null, payment.getOrder().getId(),
                payment.getOriginalOrderPrice().getValue());
    }

    public Long getId() {
        return this.id;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public int getOriginalTotalPrice() {
        return this.originalTotalPrice;
    }

}
