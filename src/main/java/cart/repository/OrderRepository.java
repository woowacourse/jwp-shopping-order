package cart.repository;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderInfoDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderInfo;
import cart.dto.OrderInfoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {
    private final OrderDao orderDao;
    private final OrderInfoDao orderInfoDao;
    private final MemberDao memberDao;
    private final ProductDao productDao;
    
    public OrderRepository(final OrderDao orderDao, final OrderInfoDao orderInfoDao, final MemberDao memberDao, final ProductDao productDao) {
        this.orderDao = orderDao;
        this.orderInfoDao = orderInfoDao;
        this.memberDao = memberDao;
        this.productDao = productDao;
    }
    
    public Long insertOrder(final Member member, final Order order) {
        memberDao.updateMember(member);
        final Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        final Long memberId = memberByEmail.getId();
        final OrderEntity orderEntity = new OrderEntity(memberId, order.getOriginalPrice(), order.getUsedPoint(), order.getPointToAdd());
        
        final Long orderId = orderDao.insert(orderEntity);
        order.getOrderInfos().getOrderInfos().stream()
                .map(orderInfo -> OrderInfoEntity.of(orderId, orderInfo))
                .forEach(orderInfoDao::insert);
        return orderId;
    }
    
    public List<Order> findByMember(final Member member) {
        final Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        final List<OrderEntity> orderEntities = orderDao.findByMemberId(memberByEmail.getId());
        
        return orderEntities.stream()
                .map(orderEntity -> {
                    final Long orderId = orderEntity.getId();
                    final List<OrderInfoEntity> orderInfoEntities = orderInfoDao.findByOrderId(orderId);
                    final List<OrderInfo> orderInfos = getOrderInfos(orderInfoEntities);
                    return new Order(
                            orderId,
                            memberByEmail,
                            orderInfos,
                            orderEntity.getOriginalPrice(),
                            orderEntity.getUsedPoint(),
                            orderEntity.getPointToAdd()
                    );
                })
                .collect(Collectors.toUnmodifiableList());
    }
    
    private List<OrderInfo> getOrderInfos(final List<OrderInfoEntity> orderInfoEntities) {
        return orderInfoEntities.stream()
                .map(this::toOrderInfo)
                .collect(Collectors.toUnmodifiableList());
    }
    
    private OrderInfo toOrderInfo(final OrderInfoEntity orderInfoEntity) {
        return OrderInfo.of(
                orderInfoEntity,
                productDao.getProductById(orderInfoEntity.getProductId())
        );
    }
}
