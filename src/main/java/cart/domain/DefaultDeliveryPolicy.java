package cart.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final DefaultDeliveryPolicy that = (DefaultDeliveryPolicy) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
