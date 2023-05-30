package cart.order.presentation;

import cart.order.Order;
import cart.order.OrderCoupon;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderResponse {
    private Long orderId;
    private List<OrderItemResponse> products;
    private List<OrderCoupon> coupons;
    private DeliveyPriceResponse deliveryPrice;
    private String orderedTime;

    public OrderResponse() {
    }

    public OrderResponse(Long orderId, List<OrderItemResponse> products, List<OrderCoupon> coupons, DeliveyPriceResponse deliveryPrice, String orderedTime) {
        this.orderId = orderId;
        this.products = products;
        this.coupons = coupons;
        this.deliveryPrice = deliveryPrice;
        this.orderedTime = orderedTime;
    }

    public static OrderResponse from(Order order) {
        final var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return new OrderResponse(
                order.getId(),
                OrderItemResponse.from(order.getOrderItems()),
                order.getOrderCoupons(),
                DeliveyPriceResponse.from(order.getDeliveryPrice()),
                simpleDateFormat.format(order.getOrderedTime())
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }

    public List<OrderCoupon> getCoupons() {
        return coupons;
    }

    public DeliveyPriceResponse getDeliveryPrice() {
        return deliveryPrice;
    }

    public String getOrderedTime() {
        return orderedTime;
    }
}
