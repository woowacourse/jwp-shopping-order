package cart.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderedItemDao;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.entity.Order;

@Repository
@Transactional(readOnly = true)
public class OrderRepository {
    private final OrderDao orderDao;
    private final CartItemDao cartItemDao;
    private final OrderedItemDao orderedItemDao;

    public OrderRepository(
            OrderDao orderDao,
            CartItemDao cartItemDao,
            OrderedItemDao orderedItemDao
    ) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
        this.orderedItemDao = orderedItemDao;
    }

    @Transactional
    public Long saveOrder(List<Long> cartItemIds, Member member, Integer price, CartItems cartItems) {
        final Order order = new Order(price, member.getId());
        final Long orderId = orderDao.addOrder(order);
        orderedItemDao.saveAll(cartItems.getCartItems(), orderId);
        cartItemDao.deleteByIds(cartItemIds);
        return orderId;
    }
}
