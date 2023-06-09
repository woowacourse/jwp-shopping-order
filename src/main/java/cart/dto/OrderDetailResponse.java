package cart.dto;

import cart.domain.Order;
import cart.domain.PaymentRecord;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailResponse {

    private final List<OrderItemResponse> productList;
    private final long id;
    private final LocalDateTime orderTime;
    @JsonProperty("paymentAmount")
    private final PaymentResponse paymentResponse;

    public OrderDetailResponse(final List<OrderItemResponse> productList, final long id,
        final LocalDateTime orderTime,
        final PaymentResponse paymentResponse) {
        this.productList = productList;
        this.id = id;
        this.orderTime = orderTime;
        this.paymentResponse = paymentResponse;
    }

    public static OrderDetailResponse from(PaymentRecord paymentRecord) {
        Order order = paymentRecord.getOrder();
        List<OrderItemResponse> productList = order.getOrderItems().stream()
            .map(OrderItemResponse::from)
            .collect(Collectors.toList());
        Long id = order.getId();
        LocalDateTime orderTime = order.getOrderTime();
        PaymentResponse paymentResponse = PaymentResponse.of(paymentRecord);
        return new OrderDetailResponse(productList, id, orderTime, paymentResponse);
    }

    public List<OrderItemResponse> getProductList() {
        return this.productList;
    }

    public long getId() {
        return this.id;
    }

    public LocalDateTime getOrderTime() {
        return this.orderTime;
    }

    public PaymentResponse getPaymentResponse() {
        return this.paymentResponse;
    }
}
