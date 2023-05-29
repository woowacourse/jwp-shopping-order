package cart.domain;

import cart.exception.InvalidPriceException;

public class TotalPrice {

    private final long orderPrice;
    private final long deliveryFee;

    public TotalPrice(final long orderPrice, final long deliveryFee) {
        validate(orderPrice);
        validate(deliveryFee);
        this.orderPrice = orderPrice;
        this.deliveryFee = deliveryFee;
    }

    public void validate(final long price) {
        if (price <= 0) {
            throw new InvalidPriceException("가격은 0이하가 될 수 없습니다");
        }
    }

    public TotalPrice discountAmount(final long amount) {
        return new TotalPrice(orderPrice - amount, deliveryFee);
    }

    public TotalPrice discountPercent(final int percent) {
        return new TotalPrice(orderPrice * 100 / percent, deliveryFee);
    }

    public TotalPrice discountDelivery() {
        return new TotalPrice(orderPrice, 0);
    }

    public long getTotalItemsPrice() {
        return orderPrice;
    }

    public long getDeliveryPrice() {
        return deliveryFee;
    }

    public TotalPrice subOrderPrice(final TotalPrice other) {
        return new TotalPrice(this.orderPrice - other.orderPrice, this.deliveryFee);
    }

    public long getOrderPrice() {
        return orderPrice;
    }

    public long getDeliveryFee() {
        return deliveryFee;
    }
}
