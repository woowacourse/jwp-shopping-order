package cart.order.repository;

import cart.member.dao.MemberDao;
import cart.member.repository.MemberEntity;
import cart.order.dao.OrderDao;
import cart.order.dao.OrderInfoDao;
import cart.order.domain.Order;
import cart.order.domain.OrderInfo;
import cart.order.dto.OrderInfoEntity;
import cart.productpoint.dao.ProductPointDao;
import cart.productpoint.repository.ProductPointEntity;
import cart.product.dao.ProductDao;
import cart.member.domain.Member;
import cart.product.domain.Product;
import cart.product.repository.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {
    private final OrderDao orderDao;
    private final OrderInfoDao orderInfoDao;
    private final MemberDao memberDao;
    private final ProductDao productDao;
    private final ProductPointDao productPointDao;
    
    public OrderRepository(final OrderDao orderDao, final OrderInfoDao orderInfoDao, final MemberDao memberDao, final ProductDao productDao, final ProductPointDao productPointDao) {
        this.orderDao = orderDao;
        this.orderInfoDao = orderInfoDao;
        this.memberDao = memberDao;
        this.productDao = productDao;
        this.productPointDao = productPointDao;
    }
    
    public Long save(final Long memberId, final Order order) {
        final OrderEntity orderEntity = new OrderEntity(memberId, order.getOriginalPrice(), order.getUsedPoint(), order.getPointToAdd());
        
        final Long orderId = orderDao.insert(orderEntity);
        order.getOrderInfos().getOrderInfos().stream()
                .map(orderInfo -> OrderInfoEntity.of(orderId, orderInfo))
                .forEach(orderInfoDao::insert);
        return orderId;
    }
    
    public List<Order> findByMember(final Member member) {
        final MemberEntity memberEntity = memberDao.getMemberByEmail(member.getEmail());
        final List<OrderEntity> orderEntities = orderDao.findByMemberId(memberEntity.getId());
        
        return orderEntities.stream()
                .map(orderEntity -> {
                    final Long orderId = orderEntity.getId();
                    final List<OrderInfoEntity> orderInfoEntities = orderInfoDao.findByOrderId(orderId);
                    final List<OrderInfo> orderInfos = getOrderInfos(orderInfoEntities);
                    return new Order(
                            orderId,
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
        final ProductEntity productById = productDao.getProductById(orderInfoEntity.getProductId());
        final ProductPointEntity productPointEntity = productPointDao.getProductPointById(productById.getProductPointId());
        return OrderInfo.of(
                orderInfoEntity,
                Product.of(productById, productPointEntity)
        );
    }
    
    public Order findByMemberAndId(final Member member, final Long orderId) {
        final MemberEntity memberEntity = memberDao.getMemberByEmail(member.getEmail());
        return orderDao.findByMemberId(memberEntity.getId()).stream()
                .filter(orderEntity -> Objects.equals(orderEntity.getId(), orderId))
                .map(orderEntity -> new Order(
                        orderId,
                        getOrderInfos(orderId),
                        orderEntity.getOriginalPrice(),
                        orderEntity.getUsedPoint(),
                        orderEntity.getPointToAdd()
                ))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(member.getEmail() + " 회원님은 주문번호가 " + orderId + "인 주문 상세를 가지고있지 않습니다."));
    }
    
    private List<OrderInfo> getOrderInfos(final Long orderId) {
        return orderInfoDao.findByOrderId(orderId).stream()
                .map(orderInfoEntity -> {
                    final ProductEntity productEntity = productDao.getProductById(orderInfoEntity.getProductId());
                    final ProductPointEntity productPointEntity = productPointDao.getProductPointById(productEntity.getProductPointId());
                    return OrderInfo.of(
                            orderInfoEntity,
                            Product.of(productEntity, productPointEntity)
                    );
                })
                .collect(Collectors.toUnmodifiableList());
    }
}
