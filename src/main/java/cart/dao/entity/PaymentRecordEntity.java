package cart.dao.entity;

import cart.domain.PaymentRecord;

public class PaymentRecordEntity {

    private final Long id;
    private final Long orderId;
    private final int originalTotalPrice;

    public PaymentRecordEntity(final Long id, final Long orderId, final int originalTotalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.originalTotalPrice = originalTotalPrice;
    }

    public static PaymentRecordEntity from(final PaymentRecord paymentRecord) {
        return new PaymentRecordEntity(null, paymentRecord.getOrder().getId(), paymentRecord.getOriginalTotalPrice().getValue());
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
