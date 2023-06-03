package cart.dto;

import cart.domain.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "주문")
public class OrdersDto {

    @Schema(description = "주문 Id", example = "1")
    private final Long id;

    @Schema(description = "총 주문 가격", example = "10000")
    private final Long totalItemsPrice;

    @Schema(description = "주문 할인 가격", example = "3000")
    private final Long discountPrice;

    @Schema(description = "배달료", example = "3000")
    private final Long deliveryFee;

    @Schema(description = "주문 상품들")
    private final List<OrderItemDto> orderItems;

    public OrdersDto(final Order order) {
        this.id = order.getId();
        this.totalItemsPrice = order.getTotalPrice();
        this.discountPrice = order.getDiscountPrice();
        this.deliveryFee = order.getDeliveryFee();
        this.orderItems = order.getOrderItems().stream()
                .map(it -> new OrderItemDto(it.getId(), it.getName(), it.getPrice(), it.getImageUrl(),
                        it.getQuantity()))
                .collect(Collectors.toList());
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

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }
}
