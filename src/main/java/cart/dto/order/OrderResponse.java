package cart.dto.order;

import cart.dto.coupon.CouponResponse;
import cart.dto.product.DeliveryPrice;
import cart.dto.product.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private final long orderId;
    private final LocalDateTime orderedTime;
    private final List<ProductResponse> products;
    private final DeliveryPrice deliveryPrice;
    private final List<CouponResponse> coupons;

    public OrderResponse(final long orderId, final LocalDateTime orderedTime, final List<ProductResponse> products, final DeliveryPrice deliveryPrice, final List<CouponResponse> coupons) {
        this.orderId = orderId;
        this.orderedTime = orderedTime;
        this.products = products;
        this.deliveryPrice = deliveryPrice;
        this.coupons = coupons;
    }

    public long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public DeliveryPrice getDeliveryPrice() {
        return deliveryPrice;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
