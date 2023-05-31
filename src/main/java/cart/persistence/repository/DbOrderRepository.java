package cart.persistence.repository;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.OrderRepository;
import cart.persistence.dao.CouponDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.dao.OrderDao;
import cart.persistence.dao.OrderProductDao;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.OrderProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbOrderRepository implements OrderRepository { // TODO dao 의존 ...이거맞음?
    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;
    private final CouponDao couponDao;
    private final MemberDao memberDao;

    public DbOrderRepository(OrderDao orderDao, OrderProductDao orderProductDao, CouponDao couponDao, MemberDao memberDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.couponDao = couponDao;
        this.memberDao = memberDao;
    }

    @Override
    public List<Order> findOrderByMemberId(Long memberId) {
        List<OrderEntity> orderEntities = orderDao.findOrderByMemberId(memberId);
        return orderEntities.stream()
                .map(this::mapToOrder)
                .collect(Collectors.toList());
    }

    @Override
    public Order findOrderById(Long id) {
        OrderEntity orderEntity = orderDao.findOrderById(id);
        return mapToOrder(orderEntity);
    }

    @Override
    public Long add(Order order) {
        long orderId = orderDao.add(mapToOrderEntity(order));
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            orderProductDao.add(mapToOrderProductEntity(orderProduct, orderId));
        }
        return orderId;
    }

    @Override
    public void delete(Long id) {
        orderDao.delete(id);
    }

    private Order mapToOrder(OrderEntity orderEntity) {
        List<OrderProductEntity> orderProductEntities = orderProductDao.findOrderProductsByOrderId(orderEntity.getId());
        List<OrderProduct> orderProducts = orderProductEntities.stream() // TODO entity to model 로직 개선 필요
                .map(orderProductEntity -> new OrderProduct(
                        orderProductEntity.getId(),
                        orderProductEntity.getName(),
                        orderProductEntity.getPrice(),
                        orderProductEntity.getImageUrl(),
                        orderProductEntity.getQuantity()
                )).collect(Collectors.toList());
        CouponEntity couponEntity = couponDao.findById(orderEntity.getUsedCouponId());
        MemberEntity memberEntity = memberDao.findById(orderEntity.getMemberId());
        return new Order(
                orderEntity.getId(),
                orderEntity.getOriginalPrice(),
                orderEntity.getDiscountPrice(),
                orderProducts,
                CouponMapper.mapToCoupon(couponEntity),
                orderEntity.getConfirmState(),
                new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword())
        );
    }

    private OrderEntity mapToOrderEntity(Order order) {
        return new OrderEntity(
                order.getId(),
                order.getOriginalPrice(),
                order.getDiscountPrice(),
                order.getConfirmState(),
                order.getUsedCoupon().getId(),
                order.getMember().getId()
        );
    }

    private OrderProductEntity mapToOrderProductEntity(OrderProduct orderProduct, Long orderId) {
        return new OrderProductEntity(
                orderProduct.getId(),
                orderProduct.getName(),
                orderProduct.getPrice(),
                orderProduct.getImageUrl(),
                orderProduct.getQuantity(),
                orderId
        );
    }
}
