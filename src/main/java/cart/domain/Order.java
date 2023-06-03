package cart.domain;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.SingleCoupon;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class Order {
    private final static Price DELIVERY_FEE = Price.DELIVERY;
    private final List<OrderCartItem> orderCartItems;
    private final Coupon coupon;
    private final Timestamp time;

    public Order(List<OrderCartItem> orderCartItems, Coupon coupon) {
        this.orderCartItems = orderCartItems;
        if (!coupon.getCategory().equals(SingleCoupon.CATEGORY)) {
            throw new IllegalArgumentException("지원하지 않는 쿠폰입니다.");
        }
        this.coupon = coupon;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public Order(List<OrderCartItem> orderCartItems) {
        this(orderCartItems, null);
    }

    public List<OrderCartItem> getOrderCartItems() {
        return Collections.unmodifiableList(orderCartItems);
    }

    public Price getDiscountPriceFromTotal() {
        if (coupon == null) {
            return new Price(0);
        }
        return new Price(coupon.getDiscountValue());
    }

    public Price getDeliveryPrice() {
        Price originTotalPrice = orderCartItems.stream().
                map(OrderCartItem::getOriginalPrice)
                .reduce(Price::plus)
                .get();

        if (originTotalPrice.isMoreThan(new Price(30000))) {
            return new Price(0);
        }
        return DELIVERY_FEE;
    }

    public Price getTotalPrice() {
        return orderCartItems.stream()
                .map(OrderCartItem::getDiscountedPrice)
                .reduce(Price::plus)
                .get()
                .plus(getDeliveryPrice());
    }

    public Timestamp getTime() {
        return time;
    }
}
