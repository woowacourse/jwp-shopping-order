package cart.order_item.application;

import cart.cart_item.application.CartItemService;
import cart.cart_item.domain.CartItem;
import cart.member.domain.Member;
import cart.order.domain.Order;
import cart.order_item.application.mapper.OrderItemMapper;
import cart.order_item.dao.OrderItemDao;
import cart.order_item.dao.entity.OrderItemEntity;
import cart.order_item.domain.OrderItem;
import cart.order_item.domain.OrderedItems;
import cart.order_item.exception.CanNotOrderNotInCart;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderItemCommandService {

  private final OrderItemDao orderItemDao;
  private final CartItemService cartItemService;
  private final OrderItemQueryService orderItemQueryService;

  public OrderItemCommandService(
      final OrderItemDao orderItemDao,
      final CartItemService cartItemService,
      final OrderItemQueryService orderItemQueryService
  ) {
    this.orderItemDao = orderItemDao;
    this.cartItemService = cartItemService;
    this.orderItemQueryService = orderItemQueryService;
  }

  public OrderedItems registerOrderItem(
      final List<Long> cartItemIds,
      final Order order,
      final Member member
  ) {

    final List<CartItem> cartItems = cartItemService.findCartItemByCartIds(cartItemIds, member);

    validateAllCartItemInOrder(cartItemIds, cartItems);

    final List<OrderItemEntity> orderItemEntities = OrderItemMapper.mapToOrderItemEntities(
        cartItems,
        order
    );
    orderItemDao.save(orderItemEntities);

    final List<OrderItem> orderItems = OrderItemMapper.mapToOrderItems(orderItemEntities, order);
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

  public void deleteBatch(final Order order) {
    final List<Long> orderItemIds = orderItemQueryService.searchOrderItemsByOrderId(order)
        .stream()
        .map(OrderItem::getId)
        .collect(Collectors.toList());

    orderItemDao.deleteBatch(orderItemIds);
  }
}
