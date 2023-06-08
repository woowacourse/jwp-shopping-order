package cart.domain;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

public class PaymentRecord {
    private final Order order;
    private final List<DiscountPolicies> discountPolicies;
    private final DeliveryPolicies deliveryPolicy;

    public PaymentRecord(Order order, List<DiscountPolicies> discountPolicies, DeliveryPolicies deliveryPolicy) {
        this.order = order;
        this.discountPolicies = discountPolicies;
        this.deliveryPolicy = deliveryPolicy;
    }

    public static PaymentRecord from(Order newOrder) {
        List<DiscountPolicies> applicablePolicies = DiscountPolicies.getApplicablePolicies(newOrder);
        DeliveryPolicies deliveryPolicy = DeliveryPolicies.from(newOrder);
        return new PaymentRecord(newOrder, applicablePolicies, deliveryPolicy);
    }

    public Map<DiscountPolicies, Money> getPolicyToDiscountAmounts() {
        return this.discountPolicies.stream()
                .collect(toMap(discountPolicy -> discountPolicy,
                        discountPolicy -> discountPolicy.calculateDiscountAmount(this.order)));
    }

    public Money calculateDiscountedPrice() {
        Money discountAmount = discountPolicies.stream()
                .map(deliveryPolicy -> deliveryPolicy.calculateDiscountAmount(this.order))
                .reduce(Money.from(0), Money::add);

        return order.calculateOriginalTotalPrice().subtract(discountAmount);
    }

    public Money calculateDeliveryFee() {
        return deliveryPolicy.calculateDeliveryFee(order);
    }

    public Money calculateFinalPrice() {
        return calculateDiscountedPrice().add(calculateDeliveryFee());
    }

    public Order getOrder() {
        return order;
    }

    public Money getOriginalOrderPrice() {
        return order.calculateOriginalTotalPrice();
    }

    public List<DiscountPolicies> getDiscountPolicies() {
        return discountPolicies;
    }

    public DeliveryPolicies getDeliveryPolicy() {
        return deliveryPolicy;
    }
}
