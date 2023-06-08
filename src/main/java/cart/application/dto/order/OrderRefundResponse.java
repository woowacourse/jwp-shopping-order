package cart.application.dto.order;

import java.math.BigDecimal;

public class OrderRefundResponse {

    private final BigDecimal refundPrice;

    public OrderRefundResponse(final BigDecimal refundPrice) {
        this.refundPrice = refundPrice;
    }

    public BigDecimal getRefundPrice() {
        return refundPrice;
    }
}
