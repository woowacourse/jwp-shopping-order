package cart.repository;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.repository.dto.OrderEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final MemberDao memberDao;

    public OrderRepository(OrderDao orderDao, OrderItemDao orderItemDao, MemberDao memberDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.memberDao = memberDao;
    }

    public Order save(Order order) {
        OrderEntity orderEntity = OrderEntity.from(order);
        Long orderId = orderDao.save(orderEntity);
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemDao.save(orderId, orderItem);
        }
        return new Order(orderId, order.getMember(), order.getOrderItems(), order.getSpendPoint().getAmount(),
                order.getCreatedAt());
    }

    public Order findById(Long orderId) {
        OrderEntity orderEntity = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);// TODO
        List<OrderItem> orderItems = orderItemDao.findByOrderId(orderId);
        Member member = memberDao.findById(orderEntity.getMemberId())
                .orElseThrow(IllegalArgumentException::new);// TODO
        return new Order(orderId, member, orderItems, orderEntity.getSpendPoint(), orderEntity.getCreatedAt());
    }
}
