package cart.domain;

import java.util.List;

public class Payment {

    private final Long id;
    private final Money originalTotalPrice;
    private final Money discountAmount;
    private final Money deliveryFee;

    public Payment(Long id, Money originalTotalPrice, Money discountAmount, Money deliveryFee) {
        this.id = id;
        this.originalTotalPrice = originalTotalPrice;
        this.discountAmount = discountAmount;
        this.deliveryFee = deliveryFee;
    }

    public Payment(Money originalTotalPrice, Money discountAmount, Money deliveryFee) {
        this(null, originalTotalPrice, discountAmount, deliveryFee);
    }


    public static Payment of(Order order, List<DiscountPolicy> discountPolicies, DeliveryPolicy deliveryPolicy) {
        Money originalTotalPrice = order.calculateOriginalTotalPrice();
        Money discountAmount = discountPolicies.stream()
                .map(discountPolicy -> discountPolicy.calculateDiscountAmount(order))
                .reduce(Money.from(0), Money::add);
        Money deliveryFee = deliveryPolicy.calculateDeliveryFee(order);
        return new Payment(null, originalTotalPrice, discountAmount, deliveryFee);
    }
}
