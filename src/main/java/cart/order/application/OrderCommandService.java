package cart.order.application;

import cart.member.domain.Member;
import cart.order.application.dto.RegisterOrderRequest;
import cart.order.dao.OrderDao;
import cart.order.dao.entity.OrderEntity;
import cart.order.domain.Order;
import cart.order.exception.NotSameTotalPriceException;
import cart.order_item.application.OrderItemCommandService;
import cart.order_item.domain.OrderedItems;
import cart.value_object.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderCommandService {

  private final OrderDao orderDao;
  private final OrderItemCommandService orderItemCommandService;

  public OrderCommandService(
      final OrderDao orderDao,
      final OrderItemCommandService orderItemCommandService
  ) {
    this.orderDao = orderDao;
    this.orderItemCommandService = orderItemCommandService;
  }

  public Long registerOrder(final Member member, final RegisterOrderRequest registerOrderRequest) {

    final OrderEntity orderEntity = new OrderEntity(member.getId(),
        registerOrderRequest.getDeliveryFee());

    final Long savedOrderId = orderDao.save(orderEntity);

    final Order order = new Order(
        savedOrderId,
        member,
        new Money(registerOrderRequest.getDeliveryFee())
    );

    final OrderedItems orderedItems = orderItemCommandService.registerOrderItem(
        registerOrderRequest.getCartItemIds(),
        order,
        member
    );

    validateSameTotalPrice(orderedItems.calculateAllItemPrice(), registerOrderRequest);

    return savedOrderId;
  }

  private void validateSameTotalPrice(
      final Money totalPrice,
      final RegisterOrderRequest registerOrderRequest
  ) {
    final Money other = new Money(registerOrderRequest.getTotalPrice());

    if (totalPrice.isNotSame(other)) {
      throw new NotSameTotalPriceException("주문된 총액이 올바르지 않습니다.");
    }
  }
}
