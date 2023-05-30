package cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailResponse {
    private final List<OrderItemResponse> orderItemResponseList;
    private final long orderId;
    private final LocalDateTime orderTime;
    @JsonProperty("paymentAmount")
    private final PaymentResponse paymentResponse;

    public OrderDetailResponse(List<OrderItemResponse> orderItemResponseList, long orderId, LocalDateTime orderTime,
                               PaymentResponse paymentResponse) {
        this.orderItemResponseList = orderItemResponseList;
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.paymentResponse = paymentResponse;
    }

    public List<OrderItemResponse> getOrderItemResponseList() {
        return orderItemResponseList;
    }

    public long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public PaymentResponse getPaymentResponse() {
        return paymentResponse;
    }
}
