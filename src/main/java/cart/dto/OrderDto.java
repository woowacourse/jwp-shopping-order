package cart.dto;

import cart.domain.Product;
import cart.domain.order.OrderItem;
import java.time.LocalDateTime;

public class OrderDto {

    private Long orderId;
    private LocalDateTime orderTime;
    private Long orderProductPrice;
    private Long orderDiscountPrice;
    private Long orderDeliveryFee;
    private Long orderTotalPrice;
    private Long orderITemId;
    private String orderItemName;
    private int orderItemPrice;
    private String orderItemImageUrl;
    private int orderItemQuantity;

    public OrderDto(final Long orderId, final LocalDateTime orderTime, final Long orderProductPrice,
        final Long orderDiscountPrice,
        final Long orderDeliveryFee, final Long orderTotalPrice, final Long orderITemId, final String orderItemName,
        final int orderItemPrice,
        final String orderItemImageUrl, final int orderItemQuantity) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.orderProductPrice = orderProductPrice;
        this.orderDiscountPrice = orderDiscountPrice;
        this.orderDeliveryFee = orderDeliveryFee;
        this.orderTotalPrice = orderTotalPrice;
        this.orderITemId = orderITemId;
        this.orderItemName = orderItemName;
        this.orderItemPrice = orderItemPrice;
        this.orderItemImageUrl = orderItemImageUrl;
        this.orderItemQuantity = orderItemQuantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return new Product(orderItemName, orderItemPrice, orderItemImageUrl);
    }

    public OrderItem getOrderItem() {
        return OrderItem.persisted(orderITemId, getProduct(), orderItemQuantity);
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public Long getOrderProductPrice() {
        return orderProductPrice;
    }

    public Long getOrderDiscountPrice() {
        return orderDiscountPrice;
    }

    public Long getOrderDeliveryFee() {
        return orderDeliveryFee;
    }

    public Long getOrderTotalPrice() {
        return orderTotalPrice;
    }
}
