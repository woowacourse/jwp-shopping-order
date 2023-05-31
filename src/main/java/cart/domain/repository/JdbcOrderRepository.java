package cart.domain.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.dao.entity.OrderDetailEntity;
import cart.dao.entity.OrderEntity;
import cart.domain.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final ProductDao productDao;
    private final OrderDao orderDao;
    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;

    public JdbcOrderRepository(final ProductDao productDao, final OrderDao orderDao, final MemberDao memberDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.orderDao = orderDao;
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
    }

    @Override
    public Product findProductById(final Long id) {
        return productDao.getProductById(id);
    }

    @Override
    public Long saveOrder(final Order order) {
        return orderDao.saveOrder(order);
    }

    @Override
    public void updateMemberPoint(final Member member, final Point newPoint) {
        final Long memberId = member.getId();
        memberDao.updatePoint(memberId, newPoint.getPoint());
    }

    @Override
    public void deleteCartItemByMember(final Member member) {
        cartItemDao.deleteByMemberId(member.getId());
    }

    @Override
    public List<Order> findOrdersByMemberId(final Member member) {
        final List<OrderEntity> ordersByMemberId = orderDao.findOrdersByMemberId(member.getId());
        return ordersByMemberId.stream()
                .map(orderEntity -> new Order(
                        orderEntity.getId(),
                        findOrderDetailsByOrderId(orderEntity.getId()),
                        new Payment(BigDecimal.valueOf(orderEntity.getPayment())),
                        new Point(BigDecimal.valueOf(orderEntity.getDiscountPoint())),
                        memberDao.getMemberById(orderEntity.getMemberId())
                ))
                .collect(Collectors.toList());
    }

    private List<OrderDetail> findOrderDetailsByOrderId(final Long orderId) {
        final List<OrderDetailEntity> orderDetailsByOrderId = orderDao.findOrderDetailsByOrderId(orderId);
        return orderDetailsByOrderId.stream()
                .map(orderDetailEntity -> new OrderDetail(
                        productDao.getProductById(orderDetailEntity.getProductId()),
                        orderDetailEntity.getQuantity()
                ))
                .collect(Collectors.toList());

    }

    @Override
    public Order findOrderByOrderId(final Long orderId, Member member) {
        OrderEntity orderEntity = orderDao.findOrderById(orderId);
        final List<OrderDetail> orderDetailsByOrderId = findOrderDetailsByOrderId(orderId);
        return new Order(orderId,
                orderDetailsByOrderId,
                new Payment(BigDecimal.valueOf(orderEntity.getPayment())),
                new Point(BigDecimal.valueOf(orderEntity.getDiscountPoint())),
                member);
    }
}
