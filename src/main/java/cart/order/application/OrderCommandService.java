package cart.order.application;

import cart.member.domain.Member;
import cart.order.application.dto.RegisterOrderRequest;
import cart.order.dao.OrderDao;
import cart.order.dao.entity.OrderEntity;
import cart.order_item.application.OrderItemCommandService;
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

    orderItemCommandService.registerOrderItem(
        registerOrderRequest.getCartItemIds(),
        savedOrderId,
        member
    );

    return savedOrderId;
  }
}
