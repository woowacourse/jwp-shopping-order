package cart.domain.order.payment;

import cart.domain.Money;
import cart.domain.order.Order;
import cart.exception.DiscountPolicyException.NotFound;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DiscountPolicy {
    TEN_PERCENT_DISCOUNT_WHEN_PRICE_IS_UPPER_THAN_FIFTY_THOUSANDS(
            1L, "5만원 이상 구매 시 10% 할인",
            order -> order.calculateOriginalTotalPrice().isGreaterThanOrEqual(Money.from(50_000)),
            order -> order.calculateOriginalTotalPrice().multiply(0.1)
    );

    private final Long id;
    private final String name;
    Predicate<Order> orderTester;
    Function<Order, Money> discountCalculator;

    DiscountPolicy(Long id, String name, Predicate<Order> orderTester,
                   Function<Order, Money> discountCalculator) {
        this.id = id;
        this.name = name;
        this.orderTester = orderTester;
        this.discountCalculator = discountCalculator;
    }

    public static List<DiscountPolicy> getApplicablePolicies(Order order) {
        return Arrays.stream(DiscountPolicy.values())
                .filter(discountPolicies -> discountPolicies.orderTester.test(order))
                .collect(Collectors.toList());
    }

    public static DiscountPolicy findById(Long id) {
        return Stream.of(values())
                .filter(discountPolicy -> discountPolicy.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    throw new NotFound(id);
                });
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money calculateDiscountAmount(Order order) {
        return discountCalculator.apply(order);
    }
}
