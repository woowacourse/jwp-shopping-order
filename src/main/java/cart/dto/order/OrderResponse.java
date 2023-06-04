package cart.dto.order;

import cart.domain.order.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "주문 정보")
public class OrderResponse {

    @Schema(description = "주문 id", example = "1")
    private final Long id;

    @Schema(description = "총 상품 가격", example = "100000")
    private final Long totalItemsPrice;

    @Schema(description = "할인 가격", example = "0")
    private final Long discountPrice;

    @Schema(description = "배달비", example = "3000")
    private final Long deliveryFee;

    @Schema(description = "주문 상품")
    private final List<OrderItemResponse> orderItems;

    public OrderResponse(
            final Long id,
            final Long totalItemsPrice,
            final Long discountPrice,
            final Long deliveryFee,
            final List<OrderItemResponse> orderItems
    ) {
        this.id = id;
        this.totalItemsPrice = totalItemsPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
    }

    public static OrderResponse from(final Order order) {
        final List<OrderItemResponse> items = order.getItems().stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(
                order.getId(),
                order.calculateTotalPrice().getLongValue(),
                order.calculateDiscountPrice().getLongValue(),
                order.calculateDeliveryFee().getLongValue(),
                items
        );
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id=" + id +
                ", totalItemsPrice=" + totalItemsPrice +
                ", discountPrice=" + discountPrice +
                ", deliveryFee=" + deliveryFee +
                ", orderItems=" + orderItems +
                '}';
    }

    public Long getId() {
        return id;
    }

    public Long getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
}
