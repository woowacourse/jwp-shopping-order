package cart.domain;

import cart.domain.coupon.Coupon;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final Items items;
    private final Coupon coupon;
    private final int totalPrice;
    private final int deliveryPrice;
    private final LocalDateTime orderedAt;

    public Order(final List<Item> items, final Coupon coupon, final int totalPrice, final int deliveryPrice) {
        this(null, items, coupon, totalPrice, deliveryPrice, LocalDateTime.now());
    }

    public Order(final Long id, final List<Item> items, final Coupon coupon, final int totalPrice, final int deliveryPrice, final LocalDateTime orderedAt) {
        this.id = id;
        this.items = new Items(items);
        this.coupon = coupon;
        this.totalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
        this.orderedAt = orderedAt;
    }

    public int getDiscountedTotalPrice() {
        return totalPrice - getCouponDiscountPrice();
    }

    public int getCouponDiscountPrice() {
        if (coupon == null) {
            return 0;
        }
        return (int) (totalPrice * coupon.getDiscountRate() * 0.01);
    }

    public Long getId() {
        return id;
    }

    public Items getItems() {
        return items;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
