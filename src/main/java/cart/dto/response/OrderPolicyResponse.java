package cart.dto.response;

public class OrderPolicyResponse {

    private long freeShippingThreshold;
    private long shippingFee;
    private long pointPercentage;

    public OrderPolicyResponse(final long freeShippingThreshold, final long shippingFee, final long pointPercentage) {
        this.freeShippingThreshold = freeShippingThreshold;
        this.shippingFee = shippingFee;
        this.pointPercentage = pointPercentage;
    }

    public long getFreeShippingThreshold() {
        return freeShippingThreshold;
    }

    public long getShippingFee() {
        return shippingFee;
    }

    public long getPointPercentage() {
        return pointPercentage;
    }
}
