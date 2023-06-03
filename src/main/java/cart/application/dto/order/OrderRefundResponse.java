package cart.application.dto.order;

public class OrderRefundResponse {

    private final Integer refundPrice;

    public OrderRefundResponse(final Integer refundPrice) {
        this.refundPrice = refundPrice;
    }

    public Integer getRefundPrice() {
        return refundPrice;
    }
}
