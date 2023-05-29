package cart.domain;

public class OrderPrice {

    private final long totalItemsPrice;
    private final long deliveryPrice;

    public OrderPrice(final long totalItemsPrice, final long deliveryPrice) {
        this.totalItemsPrice = totalItemsPrice;
        this.deliveryPrice = deliveryPrice;
    }

    public OrderPrice discountAmount(final long amount) {
        return new OrderPrice(totalItemsPrice - amount, deliveryPrice);
    }

    public OrderPrice discountPercent(final int percent) {
        return new OrderPrice(totalItemsPrice * 100 / percent, deliveryPrice);
    }

    public OrderPrice discountDelivery() {
        return new OrderPrice(totalItemsPrice, 0);
    }

    public long getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public long getDeliveryPrice() {
        return deliveryPrice;
    }
}
