package cart.domain.order;

import cart.domain.cart.DeliveryFee;

import java.util.List;
import java.util.Objects;

public class Order {

    private final Long id;
    private final String orderTime;
    private final List<ProductHistory> products;
    private final DeliveryFee deliveryFee;
    private final List<CouponHistory> coupons;

    public Order(final Long id, final List<ProductHistory> products, final int deliveryFee, final List<CouponHistory> coupons, final String orderTime) {
        this.id = id;
        this.products = products;
        this.deliveryFee = DeliveryFee.from(deliveryFee);
        this.coupons = coupons;
        this.orderTime = orderTime;
    }

    public Long getId() {
        return id;
    }

    public List<ProductHistory> getProducts() {
        return products;
    }

    public DeliveryFee getDeliveryFee() {
        return deliveryFee;
    }

    public List<CouponHistory> getCoupons() {
        return coupons;
    }

    public String getOrderTime() {
        return orderTime;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
