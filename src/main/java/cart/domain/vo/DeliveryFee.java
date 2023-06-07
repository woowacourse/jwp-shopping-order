package cart.domain.vo;

import java.util.Objects;

public class DeliveryFee {

    private static final int DEFAULT_DELIVERY_FEE = 0;
    private final int deliveryFee;

    public DeliveryFee(int deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public DeliveryFee() {
        this.deliveryFee = DEFAULT_DELIVERY_FEE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryFee that = (DeliveryFee) o;
        return deliveryFee == that.deliveryFee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryFee);
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
