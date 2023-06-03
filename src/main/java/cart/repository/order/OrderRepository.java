package cart.repository.order;

import cart.dao.order.OrderDao;
import cart.dao.order.OrderItemDao;
import cart.domain.bill.Bill;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.entity.OrderEntity;
import cart.repository.cart.CartItemRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final CartItemRepository cartItemRepository;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;


    public OrderRepository(final CartItemRepository cartItemRepository, OrderDao orderDao, OrderItemDao orderItemDao) {
        this.cartItemRepository = cartItemRepository;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public Order makeOrder(final Member member, final List<Long> cartItemIds) {
        List<CartItem> cartItems = cartItemRepository.findByIds(member, cartItemIds);
        List<OrderItem> orderItems = cartItemToOrderItems(cartItems);
        return new Order(member, orderItems);
    }

    public Long save(final Order order, final Bill bill, final List<Long> cartItemIds) {
        cartItemRepository.deleteByIds(cartItemIds);
        Long orderId = orderDao.insertOrder(order, bill);
        orderItemDao.insert(order.getOrderItems().getOrderItems(), orderId);
        return orderId;
    }

    private static List<OrderItem> cartItemToOrderItems(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem(cartItem.getProduct().getName(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getProduct().getImageUrl(),
                        cartItem.getQuantity(),
                        cartItem.getProduct().getDiscountRate()))
                .collect(Collectors.toList());
    }

    public List<Order> findByMember(final Member member) {
        List<OrderEntity> orderEntities = orderDao.findByMemberId(member.getId());
        List<Order> orders = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {
            List<OrderItem> orderItems = orderItemDao.findByOrderId(orderEntity.getId());
            orders.add(orderEntityToOrder(member, orderEntity, orderItems));
        }
        return orders;
    }

    private static Order orderEntityToOrder(final Member member, final OrderEntity orderEntity, List<OrderItem> orderItems) {
        return new Order(
                orderEntity.getId(),
                member,
                new OrderItems(orderItems)
        );
    }

    public Order findById(final Member member, final Long id) {
        OrderEntity orderEntity = orderDao.findById(id).orElseThrow(IllegalArgumentException::new);
        List<OrderItem> orderItems = orderItemDao.findByOrderId(orderEntity.getId());
        return orderEntityToOrder(member, orderEntity, orderItems);
    }
}
