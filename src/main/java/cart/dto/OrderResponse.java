package cart.dto;

import cart.domain.Money;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "주문 Response")
public class OrderResponse {

    @Schema(description = "주문 id", example = "1")
    private final Long id;
    @Schema(description = "총 금액", example = "50000")
    private final long totalItemPrice;
    @Schema(description = "할인 금액", example = "3000")
    private final long discountPrice;
    @Schema(description = "배달비", example = "3000")
    private final long deliveryFee;
    private final List<OrderItemDto> orderItems;

    public OrderResponse(final Long id, final long totalItemPrice, final long discountPrice, final long deliveryFee, final List<OrderItemDto> orderItems) {
        this.id = id;
        this.totalItemPrice = totalItemPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
    }

    public static OrderResponse of(final Order order, final Money orderPrice, final Money discountedOrderPrice, final Money discountedDeliveryFee) {
        return new OrderResponse(
                order.getId(),
                orderPrice.getValue(),
                discountedOrderPrice.getValue(),
                discountedDeliveryFee.getValue(),
                mapToOrderItemDtos(order.getOrderItems().getOrderItems())
        );
    }

    private static List<OrderItemDto> mapToOrderItemDtos(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public long getTotalItemPrice() {
        return totalItemPrice;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }

    public long getDeliveryFee() {
        return deliveryFee;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }
}
