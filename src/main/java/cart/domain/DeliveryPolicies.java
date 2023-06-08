package cart.domain;

import java.util.function.Function;
import java.util.stream.Stream;

public enum DeliveryPolicies {
    DEFAULT(1L, "기본 배송비", order -> Money.from(3500));

    private final Long id;
    private final String name;
    private final Function<Order, Money> deliveryFeeCalculator;

    DeliveryPolicies(Long id, String name, Function<Order, Money> deliveryFeeCalculator) {
        this.id = id;
        this.name = name;
        this.deliveryFeeCalculator = deliveryFeeCalculator;
    }

    public static DeliveryPolicies from(Order order) {
        return DEFAULT;
    }

    public static DeliveryPolicies findById(Long id) {
        return Stream.of(values())
                .filter(deliveryPolicy -> deliveryPolicy.getId().equals(id))
                .findFirst()
                .orElseThrow(); // TODO: 2023/06/08 커스텀 예외 정의하기
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
