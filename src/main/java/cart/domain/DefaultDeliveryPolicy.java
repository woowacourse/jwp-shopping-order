package cart.domain;

public class DefaultDeliveryPolicy implements DeliveryPolicy {
    private final Long id;
    private final String name;
    private final Money fee;

    public DefaultDeliveryPolicy(final String name, final Money fee) {
        this.name = name;
        this.id = null;
        this.fee = fee;
    }

    public DefaultDeliveryPolicy(final Long id, final String name, final Money fee) {
        this.id = id;
        this.name = name;
        this.fee = fee;
    }

    public static DefaultDeliveryPolicy from(final int fee) {
        return new DefaultDeliveryPolicy("기본 배송 정책", Money.from(fee));
    }

    @Override
    public Money calculateDeliveryFee(final Order order) {
        return this.fee;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public Money getFee() {
        return this.fee;
    }
}
