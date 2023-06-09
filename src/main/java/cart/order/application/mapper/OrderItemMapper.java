package cart.order.application.mapper;

import cart.cart_item.domain.CartItem;
import cart.order.application.dto.OrderItemResponse;
import cart.order.dao.entity.OrderItemEntity;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemMapper {

  private OrderItemMapper() {
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
            BigDecimal.ONE)) //TODO : 고쳐야함
        .collect(Collectors.toList());
  }
}
