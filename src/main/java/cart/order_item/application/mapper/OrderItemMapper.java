package cart.order_item.application.mapper;

import cart.cart_item.domain.CartItem;
import cart.order_item.application.dto.OrderItemResponse;
import cart.order.domain.Order;
import cart.order_item.dao.entity.OrderItemEntity;
import cart.order_item.domain.OrderItem;
import cart.value_object.Money;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemMapper {

  private OrderItemMapper() {
  }

  public static List<OrderItem> mapToOrderItems(
      final List<OrderItemEntity> orderItemEntities,
      final Order order
  ) {
    return orderItemEntities.stream()
        .map(orderItemEntity -> new OrderItem(
            order,
            orderItemEntity.getName(),
            new Money(orderItemEntity.getPrice()),
            orderItemEntity.getImageUrl(),
            orderItemEntity.getQuantity()
        ))
        .collect(Collectors.toList());
  }

  public static List<OrderItem> mapToSavedOrderItems(
      final List<OrderItemEntity> orderItemEntities,
      final Order order
  ) {
    return orderItemEntities.stream()
        .map(orderItemEntity -> new OrderItem(
            orderItemEntity.getId(), order,
            orderItemEntity.getName(), new Money(orderItemEntity.getPrice()),
            orderItemEntity.getImageUrl(), orderItemEntity.getQuantity()
        ))
        .collect(Collectors.toList());
  }

  public static List<OrderItemEntity> mapToOrderItemEntities(
      final List<CartItem> cartItems,
      final Order order
  ) {
    return cartItems.stream()
        .map(cartItem -> new OrderItemEntity(
            order.getId(),
            cartItem.getProduct().getName(),
            BigDecimal.valueOf(cartItem.getProduct().getPrice()),
            cartItem.getProduct().getImageUrl(),
            cartItem.getQuantity()
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
            orderItem.getPrice().multiply(orderItem.getQuantity()).getValue()))
        .collect(Collectors.toList());
  }
}
