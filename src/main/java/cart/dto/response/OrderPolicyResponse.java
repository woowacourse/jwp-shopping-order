package cart.dto.response;

public class OrderPolicyResponse {

    private long freeShippingThreshold;
    private long shippingFee;

    public OrderPolicyResponse(final long freeShippingThreshold, final long shippingFee) {
        this.freeShippingThreshold = freeShippingThreshold;
        this.shippingFee = shippingFee;
    }

    public long getFreeShippingThreshold() {
        return freeShippingThreshold;
    }

    public long getShippingFee() {
        return shippingFee;
    }
}
