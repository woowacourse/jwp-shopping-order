package cart.step2.order.service;

import cart.dao.CartItemRepository;
import cart.domain.CartItem;
import cart.step2.order.domain.OrderItemEntity;
import cart.step2.order.persist.OrderItemDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class OrderItemService {

    private final CartItemRepository cartItemRepository;
    private final OrderItemDao orderItemDao;

    public OrderItemService(final CartItemRepository cartItemRepository, final OrderItemDao orderItemDao) {
        this.cartItemRepository = cartItemRepository;
        this.orderItemDao = orderItemDao;
    }

    @Transactional
    public void create(final Long memberId, final List<Long> cartItemIds, final Long orderId) {
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        List<CartItem> cartItems = cartItemRepository.findByIds(cartItemIds);
        for (CartItem cartItem : cartItems) {
            orderItemEntities.add(OrderItemEntity.createNonePkOrderItemEntity(cartItem.getProduct().getId(), orderId, cartItem.getQuantity()));
        }

        cartItemRepository.batchDelete(memberId, cartItems);
        orderItemDao.batchInsert(orderItemEntities);
    }

}
