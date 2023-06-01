package cart.domain;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

public class Payment {

    private final List<DiscountPolicy> discountPolicies;
    private final List<DeliveryPolicy> deliveryPolicies;

    public Payment(List<DiscountPolicy> discountPolicies, List<DeliveryPolicy> deliveryPolicies) {
        this.discountPolicies = discountPolicies;
        this.deliveryPolicies = deliveryPolicies;
    }

    public PaymentRecord createPaymentRecord(Order order) {
        Money originalTotalPrice = order.calculateOriginalTotalPrice();
        Map<DiscountPolicy, Money> policyToDiscountAmounts = discountPolicies.stream()
                .filter(discountPolicy -> discountPolicy.canApply(order))
                .collect(toMap(discountPolicy -> discountPolicy,
                        discountPolicy -> discountPolicy.calculateDiscountAmount(order)));

        // TODO: 적용되는 정책 필터링
        Map<DeliveryPolicy, Money> policyToDeliveryFees = deliveryPolicies.stream()
                .collect(toMap(deliveryPolicy -> deliveryPolicy,
                        (DeliveryPolicy deliveryPolicy) -> deliveryPolicy.calculateDeliveryFee(order)));

        return new PaymentRecord(order, originalTotalPrice, policyToDiscountAmounts, policyToDeliveryFees);
    }
}