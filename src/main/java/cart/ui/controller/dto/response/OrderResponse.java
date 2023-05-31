package cart.ui.controller.dto.response;

import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "주문 응답")
public class OrderResponse {

    @Schema(description = "주문 ID", example = "1")
    private Long orderId;

    @Schema(description = "주문 상품 목록 정보")
    private List<OrderProductResponse> products;

    @Schema(description = "상품 전체 금액", example = "55000")
    private int totalPrice;

    @Schema(description = "사용한 포인트", example = "1000")
    private int usedPoint;

    @Schema(description = "배송비", example = "3000")
    private int deliveryFee;

    @Schema(description = "주문 날짜", example = "2023/05/31 20:02:15")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime orderedAt;

    private OrderResponse() {
    }

    private OrderResponse(
            Long orderId,
            List<OrderProductResponse> products,
            int totalPrice,
            int usedPoint,
            int deliveryFee,
            LocalDateTime orderedAt
    ) {
        this.orderId = orderId;
        this.products = products;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.deliveryFee = deliveryFee;
        this.orderedAt = orderedAt;
    }

    public static OrderResponse from(Order order) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return new OrderResponse(
                order.getId(),
                generateOrderProducts(order.getOrderProducts()),
                order.calculateTotalPrice(),
                order.getUsedPoint(),
                order.getDeliveryFee(),
                LocalDateTime.parse(order.getOrderedAt().format(dateTimeFormatter), dateTimeFormatter)
        );
    }

    private static List<OrderProductResponse> generateOrderProducts(List<OrderProduct> products) {
        return products.stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderProductResponse> getProducts() {
        return products;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
