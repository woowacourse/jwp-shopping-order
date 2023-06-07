package cart.dto.order;

import cart.dto.coupon.CouponResponse;
import cart.dto.history.OrderHistory;
import cart.dto.product.DeliveryFeeResponse;
import cart.entity.order.ProductHistoryResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long orderId;
    private final LocalDateTime orderedTime;
    private final List<ProductHistoryResponse> products;
    private final DeliveryFeeResponse deliveryPrice;
    private final List<CouponResponse> coupons;

    private OrderResponse(final long orderId, final LocalDateTime orderedTime, final List<ProductHistoryResponse> products, final DeliveryFeeResponse deliveryPrice, final List<CouponResponse> coupons) {
        this.orderId = orderId;
        this.orderedTime = orderedTime;
        this.products = products;
        this.deliveryPrice = deliveryPrice;
        this.coupons = coupons;
    }

    public OrderResponse(final LocalDateTime orderedTime, final List<ProductHistoryResponse> products, final DeliveryFeeResponse deliveryPrice, final List<CouponResponse> coupons) {
        this.orderedTime = orderedTime;
        this.products = products;
        this.deliveryPrice = deliveryPrice;
        this.coupons = coupons;
    }

    public static OrderResponse from(final OrderHistory order) {
        List<ProductHistoryResponse> products = order.getProducts().stream()
                .map(ProductHistoryResponse::from)
                .collect(Collectors.toList());

        List<CouponResponse> coupons = order.getCoupons().stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderTime(), products, DeliveryFeeResponse.from(order.getDeliveryFee()), coupons);
    }

    public long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<ProductHistoryResponse> getProducts() {
        return products;
    }

    public DeliveryFeeResponse getDeliveryPrice() {
        return deliveryPrice;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
