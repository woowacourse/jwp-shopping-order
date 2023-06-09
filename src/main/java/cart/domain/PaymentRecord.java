package cart.domain;

import java.util.Map;

public class PaymentRecord {
    private final Order order;
    private final Money originalTotalPrice;
    private final Map<DiscountPolicy, Money> policyToDiscountAmounts;
    private final Map<DeliveryPolicy, Money> policyToDeliveryFees;

    public PaymentRecord(Order order, Money originalTotalPrice, Map<DiscountPolicy, Money> policyToDiscountAmounts,
                         Map<DeliveryPolicy, Money> policyToDeliveryFees) {
        this.order = order;
        this.originalTotalPrice = originalTotalPrice;
        this.policyToDiscountAmounts = policyToDiscountAmounts;
        this.policyToDeliveryFees = policyToDeliveryFees;
    }

    public Order getOrder() {
        return order;
    }

    public Money getOriginalTotalPrice() {
        return originalTotalPrice;
    }

    public Map<DiscountPolicy, Money> getPolicyToDiscountAmounts() {
        return policyToDiscountAmounts;
    }

    public Map<DeliveryPolicy, Money> getPolicyToDeliveryFees() {
        return policyToDeliveryFees;
    }

    public Money calculateDiscountedPrice() {
        Money discountAmount = policyToDiscountAmounts.values().stream().reduce(Money.from(0), Money::add);
        return originalTotalPrice.subtract(discountAmount);
    }

    public Money calculateDeliveryFee() {
        return policyToDeliveryFees.values().stream().reduce(Money.from(0), Money::add);
    }

    public Money calculateFinalPrice() {
        return calculateDiscountedPrice().add(calculateDeliveryFee());
    }
}
