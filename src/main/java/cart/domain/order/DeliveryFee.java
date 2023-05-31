package cart.domain.order;

public class DeliveryFee {
    private final int deliveryFee;

    public DeliveryFee(final int deliveryFee) {
        validateDeliveryFee(deliveryFee);
        this.deliveryFee = deliveryFee;
    }

    private void validateDeliveryFee(final int deliveryFee) {
        if (deliveryFee < 0) {
            throw new IllegalArgumentException("배달료는 음수가 될 수 없습니다.");
        }
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
