package cart.domain.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cart.domain.order.dto.OrderResponse;
import cart.domain.order.dto.OrderedProductDto;

public class OrderItems {

    private final List<OrderItem> orderItems;

    public OrderItems(final List<OrderItem> orderItems) {
        this.orderItems = new ArrayList<>(orderItems);
    }

    public List<OrderResponse> toOrderResponses() {
        List<OrderResponse> orderResponses = new ArrayList<>();
        List<Long> orderIds = getOrderIds();
        for (Long orderId : orderIds) {
            orderResponses.add(getOrderResponse(orderId));
        }
        return orderResponses;
    }

    private List<Long> getOrderIds() {
        return orderItems.stream()
                .map(OrderItem::getOrderId)
                .sorted(Comparator.reverseOrder())
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderResponse getOrderResponse(Long orderId) {
        List<OrderItem> filteredOrderItems = getSameOrderIdFilteredOrderItems(orderId);
        OrderItem fisrtOrderItem = filteredOrderItems.get(0);
        Order order = fisrtOrderItem.getOrder();
        Long id = order.getId();
        LocalDateTime orderedDateTime = order.getCreatedAt();
        int totalPrice = order.getTotalPrice();
        List<OrderedProductDto> orderedProducts = getOrderedProductDto(filteredOrderItems);
        return new OrderResponse(id, orderedDateTime, orderedProducts, totalPrice);
    }

    private List<OrderItem> getSameOrderIdFilteredOrderItems(Long orderId) {
        return orderItems.stream()
                .filter(orderItem -> orderItem.isSameOrder(orderId))
                .collect(Collectors.toUnmodifiableList());
    }

    private List<OrderedProductDto> getOrderedProductDto(List<OrderItem> filteredOrderItems) {
        return filteredOrderItems.stream()
                .map(OrderItem::toOrderedProductDto)
                .sorted(Comparator.comparing(OrderedProductDto::getProductId).reversed())
                .collect(Collectors.toUnmodifiableList());
    }
}
