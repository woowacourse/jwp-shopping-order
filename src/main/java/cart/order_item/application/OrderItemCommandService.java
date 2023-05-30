package cart.order_item.application;

import cart.cart_item.application.CartItemService;
import cart.cart_item.domain.CartItem;
import cart.order_item.domain.OrderItem;
import cart.order_item.domain.OrderedItems;
import cart.member.domain.Member;
import cart.order.domain.Order;
import cart.order_item.dao.OrderItemDao;
import cart.order_item.dao.entity.OrderItemEntity;
import cart.order_item.exception.CanNotOrderNotInCart;
import cart.value_object.Money;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderItemCommandService {

  private final OrderItemDao orderItemDao;
  private final CartItemService cartItemService;

  public OrderItemCommandService(
      final OrderItemDao orderItemDao,
      final CartItemService cartItemService
  ) {
    this.orderItemDao = orderItemDao;
    this.cartItemService = cartItemService;
  }

  public OrderedItems registerOrderItem(
      final List<Long> cartItemIds,
      final Order order,
      final Member member
  ) {

    final List<CartItem> cartItems = cartItemService.findCartItemByCartIds(cartItemIds,
        member);

    validateAllCartItemInOrder(cartItemIds, cartItems);

    final List<OrderItemEntity> orderItemEntities = cartItems.stream()
        .map(it -> new OrderItemEntity(
            order.getId(),
            it.getProduct().getName(),
            BigDecimal.valueOf(it.getProduct().getPrice()),
            it.getProduct().getImageUrl(),
            it.getQuantity()
        ))
        .collect(Collectors.toList());

    orderItemDao.save(orderItemEntities);

    final List<OrderItem> orderItems = cartItems.stream()
        .map(it -> new OrderItem(
            order,
            it.getProduct().getName(),
            new Money(it.getProduct().getPrice()),
            it.getProduct().getImageUrl(),
            it.getQuantity()))
        .collect(Collectors.toList());

    return new OrderedItems(orderItems);
  }

  private void validateAllCartItemInOrder(
      final List<Long> cartItemIds,
      final List<CartItem> cartItems
  ) {
    if (cartItemIds.size() != cartItems.size()) {
      throw new CanNotOrderNotInCart("장바구니에 담지 않은 물품은 주문할 수 없습니다.");
    }
  }
}
