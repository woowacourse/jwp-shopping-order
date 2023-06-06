package cart.application.dto.response;

public class OrderResponse {

    private final Long orderId;
    private final Integer totalPrice;
    private final Integer totalAmount;
    private final String previewName;

    public OrderResponse(final Long orderId, final Integer totalPrice, final Integer totalAmount, final String previewName) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.totalAmount = totalAmount;
        this.previewName = previewName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public String getPreviewName() {
        return previewName;
    }
}
