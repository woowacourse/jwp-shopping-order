package cart.domain.order;

import cart.domain.coupon.Coupon;

import java.util.List;

public class Order {
    private final List<OrderProduct> products;
    private OrderPrice orderPrice;

    public Order(final List<OrderProduct> products, final OrderPrice orderPrice) {
        this.products = products;
        this.orderPrice = orderPrice;
    }

    public int price() {
        return orderPrice.calculatePrice();
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void applyCoupon(final Coupon coupon) {
        orderPrice = coupon.apply(orderPrice);
    }
}
