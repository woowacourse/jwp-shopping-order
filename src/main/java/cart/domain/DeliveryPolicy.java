package cart.domain;

import cart.exception.DeliveryPolicyException.NotFound;
import java.util.function.Function;
import java.util.stream.Stream;

public enum DeliveryPolicy {
    DEFAULT(1L, "기본 배송비", order -> Money.from(3500));

    private final Long id;
    private final String name;
    private final Function<Order, Money> deliveryFeeCalculator;

    DeliveryPolicy(Long id, String name, Function<Order, Money> deliveryFeeCalculator) {
        this.id = id;
        this.name = name;
        this.deliveryFeeCalculator = deliveryFeeCalculator;
    }

    public static DeliveryPolicy from(Order order) {
        return DEFAULT;
    }

    public static DeliveryPolicy findById(Long id) {
        return Stream.of(values())
                .filter(deliveryPolicy -> deliveryPolicy.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    throw new NotFound(id);
                });
    }

    public Money calculateDeliveryFee(Order order) {
        return deliveryFeeCalculator.apply(order);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
