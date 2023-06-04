package cart.dto.response;

import cart.domain.OrderHistory;
import cart.domain.OrderItem;

public class OrderItemResponse {

    private Long orderId;
    private Integer orderPrice;
    private Integer totalAmount;
    private String previewName;

    public OrderItemResponse() {
    }

    public OrderItemResponse(final Long orderId, final Integer orderPrice, final Integer totalAmount,
                             final String previewName) {
        this.orderId = orderId;
        this.orderPrice = orderPrice;
        this.totalAmount = totalAmount;
        this.previewName = previewName;
    }

    public static OrderItemResponse of(OrderHistory orderHistory) {
        int totalAmount = orderHistory.getOrderItems().stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
        String previewName = orderHistory.getOrderItems().get(0).getName();
        return new OrderItemResponse(orderHistory.getId(), orderHistory.getOrderPrice(), totalAmount, previewName);
    }

    public Long getOrderId() {
        return orderId;
    }

    public Integer getOrderPrice() {
        return orderPrice;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public String getPreviewName() {
        return previewName;
    }
}
