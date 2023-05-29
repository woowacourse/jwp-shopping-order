package cart.order_item.application;

import cart.cart_item.application.CartItemService;
import cart.cart_item.domain.CartItem;
import cart.member.domain.Member;
import cart.order_item.dao.OrderItemDao;
import cart.order_item.dao.entity.OrderItemEntity;
import cart.order_item.exception.CanNotOrderNotInCart;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
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

  public void registerOrderItem(final List<Long> cartItemIds, final Long orderId,
      final Member member) {

    final List<CartItem> cartItems = cartItemService.findCartItemByCartIds(cartItemIds,
        member);

    validateAllCartItemInOrder(cartItemIds, cartItems);

    final List<OrderItemEntity> orderItemEntities = cartItems.stream()
        .map(it -> new OrderItemEntity(
            orderId,
            it.getProduct().getName(),
            BigDecimal.valueOf(it.getProduct().getPrice()),
            it.getProduct().getImageUrl(),
            it.getQuantity()
        ))
        .collect(Collectors.toList());

    orderItemDao.save(orderItemEntities);
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
