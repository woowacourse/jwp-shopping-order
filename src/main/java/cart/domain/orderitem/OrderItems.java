package cart.domain.orderitem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cart.domain.order.Order;
import cart.dto.OrderResponse;
import cart.dto.ProductResponse;

public class OrderItems {

    private final List<OrderItem> orderItems;

    public OrderItems(final List<OrderItem> orderItems) {
        this.orderItems = new ArrayList<>(orderItems);
    }

    public List<OrderResponse> toOrderResponses() {
        List<OrderResponse> orderResponses = new ArrayList<>();
        List<Long> orderIds = getOrderIds();
        for (Long orderId : orderIds) {
            addOrderResponse(orderResponses, orderId);
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

    private void addOrderResponse(List<OrderResponse> orderResponses, Long orderId) {
        List<OrderItem> filteredOrderItems = getSameOrderIdFilteredOrderItems(orderId);
        OrderItem fisrtOrderItem = filteredOrderItems.get(0);
        Order order = fisrtOrderItem.getOrder();
        Long id = order.getId();
        LocalDateTime orderedDateTime = order.getCreatedAt();
        int totalPrice = order.getTotalPrice();
        List<ProductResponse> products = getProductResponses(filteredOrderItems);
        orderResponses.add(new OrderResponse(id, orderedDateTime, products, totalPrice));
    }

    private List<OrderItem> getSameOrderIdFilteredOrderItems(Long orderId) {
        return orderItems.stream()
                .filter(orderItem -> orderItem.isSameOrder(orderId))
                .collect(Collectors.toUnmodifiableList());
    }

    private List<ProductResponse> getProductResponses(List<OrderItem> filteredOrderItems) {
        return filteredOrderItems.stream()
                .map(OrderItem::toProductResponse)
                .sorted(Comparator.comparing(ProductResponse::getId).reversed())
                .collect(Collectors.toUnmodifiableList());
    }
}
