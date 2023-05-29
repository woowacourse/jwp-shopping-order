package cart.domain;

public class Order {

    private final Items items;
    private final Coupon coupon;
    private final int deliveryPrice;

    public Order(final Items items, final Coupon coupon, final int deliveryPrice) {
        this.items = items;
        this.coupon = coupon;
        this.deliveryPrice = deliveryPrice;
    }

    public Items getItems() {
        return items;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }
}
