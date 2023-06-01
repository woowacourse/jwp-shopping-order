package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MysqlOrderRepository implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final CartItemDao cartItemDao;
    private final ProductRepository productRepository;

    public MysqlOrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao, final CartItemDao cartItemDao, final MysqlProductRepository productRepository) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.cartItemDao = cartItemDao;
        this.productRepository = productRepository;
    }

    @Override
    public Order save(final Order order) {
        final OrderEntity savedOrderEntity = orderDao.createOrder(new OrderEntity(order.getMemberId(), order.getPrice()));
        final List<OrderItemEntity> orderItemEntities = order.getCartItems().stream()
                .map(cartItem -> new OrderItemEntity(savedOrderEntity.getId(), cartItem.getProductId(), cartItem.getQuantity()))
                .collect(Collectors.toList());

        orderItemDao.saveAll(orderItemEntities);
        cartItemDao.deleteByIds(order.getCartItemIds());

        return new Order(savedOrderEntity.getId(), order.getPrice(), order.getMember(), order.getCartItems());
    }

    @Override
    public Order findById(final Long id, final Member member) {
        final List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(id);
        final List<CartItem> cartItems = orderItemEntities.stream()
                .map(orderItemEntity -> new CartItem(orderItemEntity.getQuantity(), productRepository.findById(orderItemEntity.getProductId()), member))
                .collect(Collectors.toList());

        final int totalPrice = cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();

        return new Order(id, totalPrice, member, cartItems);
    }

    @Override
    public List<Order> findMemberOrders(final Member member) {
        final List<OrderEntity> orderEntities = orderDao.findByMemberId(member.getId());
        return orderEntities.stream()
                .map(orderEntity -> findById(orderEntity.getId(), member))
                .collect(Collectors.toList());
    }
}
