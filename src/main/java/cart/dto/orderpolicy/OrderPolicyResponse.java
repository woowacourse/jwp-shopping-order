package cart.dto.orderpolicy;

public class OrderPolicyResponse {
    private Long freeShippingThreshold;
    private Long shippingFee;
    private int pointPercentage;

    public OrderPolicyResponse() {
    }

    public OrderPolicyResponse(Long freeShippingThreshold, Long shippingFee, int pointPercentage) {
        this.freeShippingThreshold = freeShippingThreshold;
        this.shippingFee = shippingFee;
        this.pointPercentage = pointPercentage;
    }

    public Long getFreeShippingThreshold() {
        return freeShippingThreshold;
    }

    public Long getShippingFee() {
        return shippingFee;
    }

    public int getPointPercentage() {
        return pointPercentage;
    }
}
