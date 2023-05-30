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
        if (price < 0) {
            throw new InvalidPriceException("가격은 0보다 작을 수 없습니다");
        }
    }

    public TotalPrice discountAmount(final long amount) {
        return new TotalPrice(orderPrice - amount, deliveryFee);
    }

    public TotalPrice discountPercent(final long percent) {
        return new TotalPrice(orderPrice * 100 / percent, deliveryFee);
    }

    public TotalPrice discountDelivery(final long amount) {
        return new TotalPrice(orderPrice, amount);
    }

    public long getTotalItemsPrice() {
        return orderPrice;
    }

    public boolean orderPriceIsMoreThan(final long minimumPrice) {
        return minimumPrice >= orderPrice;
    }

    public TotalPrice subOrderPrice(final TotalPrice other) {
        return new TotalPrice(this.orderPrice - other.orderPrice, this.deliveryFee);
    }

    public long getDeliveryPrice() {
        return deliveryFee;
    }

    public long getOrderPrice() {
        return orderPrice;
    }

    public long getDeliveryFee() {
        return deliveryFee;
    }
}
