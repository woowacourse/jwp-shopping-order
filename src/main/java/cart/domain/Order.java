package cart.domain;

import cart.domain.vo.Amount;

public class Order {

    private final Long id;
    private final Products products;
    // todo : Coupon 의존성 제거
    private final Coupon coupon;
    private final Amount totalAmount;
    private final Amount deliveryAmount;
    private final String address;

    public Order(final Products products, final Coupon coupon, final Amount deliveryAmount, final Amount totalAmount,
        final String address) {
        this(null, products, coupon, deliveryAmount, totalAmount, address);
    }

    public Order(final Long id, final Products products, final Coupon coupon, final Amount totalAmount,
        final Amount deliveryAmount, final String address) {
        this.id = id;
        this.products = products;
        this.coupon = coupon;
        this.totalAmount = totalAmount;
        this.deliveryAmount = deliveryAmount;
        this.address = address;
    }

    public Amount discountProductAmount() {
        final Amount productAmount = products.calculateTotalAmount();
        return coupon.calculateProduct(productAmount);
    }

    public Long getId() {
        return id;
    }

    public Products getProducts() {
        return products;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public Amount getDeliveryAmount() {
        return deliveryAmount;
    }

    public String getAddress() {
        return address;
    }

    public Amount getTotalAmount() {
        return totalAmount;
    }
}
