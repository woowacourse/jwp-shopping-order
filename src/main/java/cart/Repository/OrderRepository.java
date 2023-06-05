package cart.Repository;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.Member.Member;
import cart.domain.Order.Order;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.ProductEntity;
import cart.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static cart.Repository.mapper.OrderMapper.toOrder;
import static cart.Repository.mapper.OrderMapper.toOrders;

@Repository
public class OrderRepository {
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;


    public OrderRepository(OrderDao orderDao, OrderItemDao orderItemDao, ProductDao productDao, MemberDao memberDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    public Long save(Member member, Order order) {
        OrderEntity orderEntity = new OrderEntity(member.getId());
        Long orderId = orderDao.save(member, orderEntity);

        List<OrderItemEntity> orderItemEntities = order.getOrderItem()
                .stream().map(it -> new OrderItemEntity(
                        orderId,
                        it.getProduct().getId(),
                        it.getQuantity().quantity(),
                        it.getPrice().price()
                )).collect(Collectors.toUnmodifiableList());

        orderItemDao.save(orderItemEntities);
        return orderId;
    }

    public List<Order> findByMemberId(Long memberId) {
        List<OrderEntity> orders = orderDao.findOrderByMemberId(memberId);

        if(orders.isEmpty()){
            return Collections.emptyList();
        }

        Map<Long, List<OrderItemEntity>> orderItemsByOrderId = orderItemDao.findOrderItemsByMemberId(memberId);

        Set<Long> productIds = new HashSet<>();
        orderItemsByOrderId.values()
                .forEach(orderItems ->
                        orderItems.forEach(orderItemEntity ->
                                productIds.add(orderItemEntity.getProductId())));

        if(productIds.isEmpty()){
            return Collections.emptyList();
        }

        List<ProductEntity> productEntities = productDao.getProductByIds(new ArrayList<>(productIds));

        MemberEntity memberEntity = memberDao.getMemberById(memberId)
                .orElseThrow(() -> new NotFoundException.Member(memberId));

        return toOrders(orders, orderItemsByOrderId, productEntities, memberEntity);
    }

    public Order findById(Long orderId) {
        OrderEntity orderEntity = getOrderEntity(orderId);

        MemberEntity memberEntity = memberDao.getMemberById(orderEntity.getMemberId())
                .orElseThrow(() -> new NotFoundException.Member(orderEntity.getMemberId()));

        List<OrderItemEntity> orderItems = orderItemDao.findOrderItemsByOrderId(orderEntity.getId());

        Set<Long> productIds = new HashSet<>();
        orderItems.forEach(orderItemEntity ->
                productIds.add(orderItemEntity.getProductId()));

        List<ProductEntity> productEntities = productDao.getProductByIds(new ArrayList<>(productIds));

        return toOrder(orderEntity, orderItems, productEntities, memberEntity);
    }

    private OrderEntity getOrderEntity(Long orderId) {
        return orderDao.findOrderById(orderId)
                .orElseThrow(() -> new NotFoundException.Order(orderId));
    }
}
