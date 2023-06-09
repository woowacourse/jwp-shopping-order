package cart.dto;

import cart.domain.Coupon;
import cart.domain.Order;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class OrderDetailResponse {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Long orderId;
    private List<OrderItemResponse> products;
    private Long totalPrice;
    private Long totalPayments;
    private Long deliveryFee;
    private CouponResponse coupon;
    private String createdAt;
    private String orderStatus;

    public OrderDetailResponse() {
    }

    public OrderDetailResponse(final Long orderId,
                               final List<OrderItemResponse> products,
                               final Long totalPrice,
                               final Long totalPayments,
                               final Long deliveryFee,
                               final CouponResponse coupon,
                               final String createdAt,
                               final String orderStatus) {
        this.orderId = orderId;
        this.products = products;
        this.totalPrice = totalPrice;
        this.totalPayments = totalPayments;
        this.deliveryFee = deliveryFee;
        this.coupon = coupon;
        this.createdAt = createdAt;
        this.orderStatus = orderStatus;
    }

    public static OrderDetailResponse from(final Order order) {
        final Coupon coupon = order.getCoupon();
        if (Objects.nonNull(coupon)) {
            return new OrderDetailResponse(
                    order.getId(),
                    OrderItemResponse.from(order.getOrderItems()),
                    order.totalProductPrice().getValue(),
                    order.totalPayments().getValue(),
                    order.getDeliveryFee().getValue(),
                    CouponResponse.from(coupon),
                    DATE_FORMAT.format(order.getCreatedAt()),
                    order.getStatus().getValue()
            );
        }
        return new OrderDetailResponse(
                order.getId(),
                OrderItemResponse.from(order.getOrderItems()),
                order.totalProductPrice().getValue(),
                order.totalPayments().getValue(),
                order.getDeliveryFee().getValue(),
                null,
                DATE_FORMAT.format(order.getCreatedAt()),
                order.getStatus().getValue()
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public Long getTotalPayments() {
        return totalPayments;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public CouponResponse getCoupon() {
        return coupon;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
