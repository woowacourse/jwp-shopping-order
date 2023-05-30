package cart.domain;

import cart.domain.vo.Amount;

public class Order {

    private final Long id;
    private final Products products;
    private final Coupon coupon;
    private final Amount deliveryAmount;
    private final String address;

    public Order(final Long id, final Products products, final Coupon coupon, final Amount deliveryAmount,
        final String address) {
        this.id = id;
        this.products = products;
        this.coupon = coupon;
        this.deliveryAmount = deliveryAmount;
        this.address = address;
    }

    public Amount discountProductAmount() {
        final Amount productAmount = products.calculateTotalAmount();
        return coupon.calculateProduct(productAmount);
    }

    public Amount discountDeliveryAmount() {
        return coupon.calculateDelivery(deliveryAmount);
    }
}
