package cart.dto.response;

public class OrderResponseDto {
    private final long orderId;

    public OrderResponseDto(final long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }
}
