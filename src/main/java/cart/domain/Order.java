package cart.domain;

import cart.domain.vo.Amount;

public class Order {

    private final Long id;
    private final Products products;
    private final MemberCoupon coupon;
    private final Amount deliveryAmount;
    private final String address;

    public Order(final Products products, final MemberCoupon coupon, final Amount deliveryAmount, final String address) {
        this(null, products, coupon, deliveryAmount, address);
    }

    public Order(final Long id, final Products products, final MemberCoupon coupon, final Amount deliveryAmount,
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

    public Long getId() {
        return id;
    }

    public Products getProducts() {
        return products;
    }

    public MemberCoupon getCoupon() {
        return coupon;
    }

    public Amount getDeliveryAmount() {
        return deliveryAmount;
    }

    public String getAddress() {
        return address;
    }
}
