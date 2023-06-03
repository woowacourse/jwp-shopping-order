package cart.domain;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class Payment {

    private final List<DiscountPolicy> discountPolicies;
    private final List<DeliveryPolicy> deliveryPolicies;

    public Payment(final List<DiscountPolicy> discountPolicies, final List<DeliveryPolicy> deliveryPolicies) {
        this.discountPolicies = discountPolicies;
        this.deliveryPolicies = deliveryPolicies;
    }

    public PaymentRecord createPaymentRecord(final Order order) {
        final Money originalTotalPrice = order.calculateOriginalTotalPrice();
        final Map<DiscountPolicy, Money> policyToDiscountAmounts = this.discountPolicies.stream()
                .filter(discountPolicy -> discountPolicy.canApply(order))
                .collect(toMap(discountPolicy -> discountPolicy,
                        discountPolicy -> discountPolicy.calculateDiscountAmount(order)));

        final Map<DeliveryPolicy, Money> policyToDeliveryFees = this.deliveryPolicies.stream()
                .collect(toMap(deliveryPolicy -> deliveryPolicy,
                        (DeliveryPolicy deliveryPolicy) -> deliveryPolicy.calculateDeliveryFee(order)));

        return new PaymentRecord(order, originalTotalPrice, policyToDiscountAmounts, policyToDeliveryFees);
    }
}