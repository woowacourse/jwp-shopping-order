package cart.order.application.mapper;

import cart.cart_item.domain.CartItem;
import cart.order.application.dto.OrderItemResponse;
import cart.order.dao.entity.OrderItemEntity;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderedItems;
import cart.value_object.Money;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemMapper {

  private OrderItemMapper() {
  }

  public static List<OrderItemEntity> mapToOrderItemEntities(
      final OrderedItems orderedItems,
      final Long orderId
  ) {
    return orderedItems.getOrderItems().stream()
        .map(orderItem -> new OrderItemEntity(
            orderId,
            orderItem.getName(),
            orderItem.getPrice().getValue(),
            orderItem.getImageUrl(),
            orderItem.getQuantity()
        ))
        .collect(Collectors.toList());
  }

  public static List<OrderItemResponse> mapToOrderItemResponse(final List<OrderItem> orderItems) {
    return orderItems.stream()
        .map(orderItem -> new OrderItemResponse(
            orderItem.getId(),
            orderItem.getName(),
            orderItem.getImageUrl(),
            orderItem.getQuantity(),
            orderItem.calculatePrice().getValue()))
        .collect(Collectors.toList());
  }

  public static List<OrderItem> mapToOrderItemsFrom(final List<CartItem> cartItems) {
    return cartItems.stream()
        .map(it -> new OrderItem(
            it.getProduct().getName(),
            new Money(it.getProduct().getPrice()),
            it.getProduct().getImageUrl(),
            it.getQuantity()))
        .collect(Collectors.toList());
  }
}
