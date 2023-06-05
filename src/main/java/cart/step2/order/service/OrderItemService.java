package cart.step2.order.service;

import cart.dao.CartItemDao;
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

    private final CartItemDao cartItemDao;
    private final OrderItemDao orderItemDao;

    public OrderItemService(final CartItemDao cartItemDao, final OrderItemDao orderItemDao) {
        this.cartItemDao = cartItemDao;
        this.orderItemDao = orderItemDao;
    }

    @Transactional
    public void create(final Long memberId, final List<Long> cartItemIds, final Long orderId) {
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        List<CartItem> removalCartItems = new ArrayList<>();

        for (Long cartItemId : cartItemIds) {
            CartItem cartItem = cartItemDao.findById(cartItemId);
            orderItemEntities.add(OrderItemEntity.createNonePkOrderItemEntity(cartItem.getProduct().getId(), orderId, cartItem.getQuantity()));
            removalCartItems.add(cartItem);
        }

        cartItemDao.batchDelete(memberId, removalCartItems);
        orderItemDao.batchInsert(orderItemEntities);
    }

}
