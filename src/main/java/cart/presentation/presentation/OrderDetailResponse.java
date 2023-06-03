package cart.presentation.presentation;

import cart.order.Order;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailResponse {
    private Long id;
    private String orderedTime;
    private List<OrderItemResponse> orderedItems;
    private int deliveryPrice;
    private int discountFromTotalPrice;

    public OrderDetailResponse() {
    }

    public OrderDetailResponse(Long id, String orderedTime, List<OrderItemResponse> orderedItems, int deliveryPrice, int discountFromTotalPrice) {
        this.id = id;
        this.orderedTime = orderedTime;
        this.orderedItems = orderedItems;
        this.deliveryPrice = deliveryPrice;
        this.discountFromTotalPrice = discountFromTotalPrice;
    }

    public static OrderDetailResponse from(Order order) {
        final var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        final var orderItemResponses = order.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());

        return new OrderDetailResponse(
                order.getId(),
                simpleDateFormat.format(order.getOrderedTime()),
                orderItemResponses,
                order.getDeliveryPrice(),
                order.getDiscountFromTotalPrice()
        );
    }
    public Long getId() {
        return id;
    }

    public String getOrderedTime() {
        return orderedTime;
    }

    public List<OrderItemResponse> getOrderedItems() {
        return orderedItems;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public int getDiscountFromTotalPrice() {
        return discountFromTotalPrice;
    }
}
