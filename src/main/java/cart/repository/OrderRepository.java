package cart.repository;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

    private final MemberDao memberDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(final MemberDao memberDao, final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.memberDao = memberDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public long save(final Order order) {
        OrderEntity orderEntity = order.toEntity();
        long orderId = orderDao.save(orderEntity);
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemDao.insert(orderId, orderItem);
        }
        return orderId;
    }

    // todo: 없는 orderId 들어온경우 처리
    // todo: Member가 삭제된 경우?
    public Order findByOrderId(final long orderId) {
        OrderEntity orderEntity = orderDao.findById(orderId);
        Member member = memberDao.getMemberById(orderEntity.getMemberId());
        List<OrderItem> orderItems = orderItemDao.findAllByOrderId(orderId);
        return new Order(orderId, member, orderEntity.getShippingFee(), orderEntity.getTotalPrice(), orderItems, orderEntity.getCreatedAt());
    }

    public List<Order> findByMemberId(final long memberId) {
        List<Order> orders = new ArrayList<>();
        Member member = memberDao.getMemberById(memberId);
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(memberId);
        for (OrderEntity orderEntity : orderEntities) {
            List<OrderItem> orderItems = orderItemDao.findAllByOrderId(orderEntity.getId());
            orders.add(new Order(
                    orderEntity.getId(),
                    member,
                    orderEntity.getShippingFee(),
                    orderEntity.getTotalPrice(),
                    orderItems,
                    orderEntity.getCreatedAt()
            ));
        }
        return orders;
    }
}
