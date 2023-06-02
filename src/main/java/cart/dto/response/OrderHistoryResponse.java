package cart.dto.response;

import java.util.List;

public class OrderHistoryResponse {

    private final Long orderId;
    private final int orderPrice;
    private final int totalAmount;
    private final String previewName;

    public OrderHistoryResponse(
            final Long orderId,
            final int orderPrice,
            final int totalAmount,
            final String previewName
    ) {
        this.orderId = orderId;
        this.orderPrice = orderPrice;
        this.totalAmount = totalAmount;
        this.previewName = previewName;
    }

    public static OrderHistoryResponse from(
            final Long orderId,
            final int totalPrice,
            final int totalAmount,
            final List<String> productNames
    ) {
        final String previewName = productNames.get(0);
        return new OrderHistoryResponse(
                orderId,
                totalPrice,
                totalAmount,
                previewName
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public String getPreviewName() {
        return previewName;
    }
}
