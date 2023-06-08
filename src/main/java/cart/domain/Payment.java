package cart.domain;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

public class Payment {
    private final Order order;
    private final List<DiscountPolicy> discountPolicies;
    private final DeliveryPolicy deliveryPolicy;

    public Payment(Order order, List<DiscountPolicy> discountPolicies, DeliveryPolicy deliveryPolicy) {
        this.order = order;
        this.discountPolicies = discountPolicies;
        this.deliveryPolicy = deliveryPolicy;
    }

    public static Payment from(Order newOrder) {
        List<DiscountPolicy> applicablePolicies = DiscountPolicy.getApplicablePolicies(newOrder);
        DeliveryPolicy deliveryPolicy = DeliveryPolicy.from(newOrder);
        return new Payment(newOrder, applicablePolicies, deliveryPolicy);
    }

    public Map<DiscountPolicy, Money> getPolicyToDiscountAmounts() {
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

    public List<DiscountPolicy> getDiscountPolicies() {
        return discountPolicies;
    }

    public DeliveryPolicy getDeliveryPolicy() {
        return deliveryPolicy;
    }
}
