package cart.dto.response;

public class OrderResponseDto {
    private long orderId;

    public OrderResponseDto() {
    }

    public OrderResponseDto(final long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }
}
