package cart.domain.order;

import cart.domain.Coupon;

import java.util.List;

public class Order {
    private final List<OrderProduct> products;
    private final OrderPrice orderPrice;

    public Order(final List<OrderProduct> products) {
        this.products = products;
        this.orderPrice = new OrderPrice(products);
    }

    public void applyDeliveryFee(final DeliveryFee deliveryFee) {
        orderPrice.calculateDeliveryFee(deliveryFee);
    }

    public void applyCoupon(final Coupon coupon) {
        orderPrice.apply(coupon);
    }

    public int price() {
        return orderPrice.calculatePrice();
    }

    public List<OrderProduct> getProducts() {
        return products;
    }
}
