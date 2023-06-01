package cart.step2.order.service;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.step2.order.domain.OrderItem;
import cart.step2.order.domain.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class OrderItemService {

    private final CartItemDao cartItemDao;
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(final CartItemDao cartItemDao, final OrderItemRepository orderItemRepository) {
        this.cartItemDao = cartItemDao;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public void create(final Long memberId, final List<Long> cartItemIds, final Long orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        List<CartItem> removalCartItems= new ArrayList<>();

        for (Long cartItemId : cartItemIds) {
            CartItem cartItem = cartItemDao.findById(cartItemId);
            orderItems.add(OrderItem.createNonePkOrder(cartItem.getProduct().getId(), orderId, cartItem.getQuantity()));
            removalCartItems.add(cartItem);
        }

        cartItemDao.batchDelete(memberId, removalCartItems);
        orderItemRepository.createAllOrderItems(orderItems);
    }

}
