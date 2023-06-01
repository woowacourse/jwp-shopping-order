package cart.domain;

import cart.exception.PolicyException.NoShippingFee;

import java.util.List;

public class ShippingPolicy {

    private final long basicShippingFee;
    private final long threshold;

    public ShippingPolicy(final long basicShippingFee, final long threshold) {
        this.basicShippingFee = basicShippingFee;
        this.threshold = threshold;
    }

    public long calculateShippingFee(final List<OrderItem> orderItems){
        long shippingFee = basicShippingFee;
        long totalProductPrice = orderItems.stream()
                .mapToLong(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        if (totalProductPrice >= threshold) {
            shippingFee = 0;
        }
        return shippingFee;
    }
}
