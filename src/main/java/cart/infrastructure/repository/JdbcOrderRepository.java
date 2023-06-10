package cart.infrastructure.repository;

import cart.domain.Coupon;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.domain.repository.OrderRepository;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.OrderEntity;
import cart.entity.ProductOrderEntity;
import cart.infrastructure.dao.CouponDao;
import cart.infrastructure.dao.MemberCouponDao;
import cart.infrastructure.dao.OrderDao;
import cart.infrastructure.dao.ProductDao;
import cart.infrastructure.dao.ProductOrderDao;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final OrderDao orderDao;
    private final ProductOrderDao productOrderDao;
    private final ProductDao productDao;
    private final MemberCouponDao memberCouponDao;
    private final CouponDao couponDao;

    public JdbcOrderRepository(final OrderDao orderDao, final ProductOrderDao productOrderDao,
                               final ProductDao productDao, final MemberCouponDao memberCouponDao,
                               final CouponDao couponDao) {
        this.orderDao = orderDao;
        this.productOrderDao = productOrderDao;
        this.productDao = productDao;
        this.memberCouponDao = memberCouponDao;
        this.couponDao = couponDao;
    }

    @Override
    public Order create(final Order order, final Long memberId) {
        final Long orderId = orderDao.create(OrderEntity.of(order, memberId), memberId);
        final List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            productOrderDao.create(ProductOrderEntity.of(orderItem, orderId));
        }
        return new Order(orderId, order.getOrderItems(), order.getCoupon(), order.getTotalProductAmount(),
                order.getDeliveryAmount(), order.getAddress());
    }

    @Override
    public Order findById(final Long id, final Long memberId) {
        final List<OrderItem> orderItems = findOrderItems(id);
        final OrderEntity orderEntity = orderDao.findById(id)
                .orElseThrow(NoSuchElementException::new);
        final MemberCoupon memberCoupon = findMemberCoupon(memberId, orderEntity);
        return orderEntity.toDomain(orderItems, memberCoupon);
    }

    private MemberCoupon findMemberCoupon(final Long memberId, final OrderEntity orderEntity) {
        final Coupon coupon = couponDao.findById(orderEntity.getCouponId())
                .orElse(CouponEntity.empty())
                .toDomain();
        return memberCouponDao.findByCouponIdAndMemberId(orderEntity.getCouponId(), memberId)
                .orElse(MemberCouponEntity.empty(memberId))
                .toDomain(coupon);
    }

    @Override
    public List<Order> findAll(final Long memberId) {
        final List<OrderEntity> orderEntities = orderDao.findAllByMemberId(memberId);
        return orderEntities.stream()
                .map(it -> it.toDomain(findOrderItems(it.getId()), findMemberCoupon(memberId, it)))
                .collect(Collectors.toList());
    }

    private List<OrderItem> findOrderItems(final Long id) {
        return productOrderDao.findAllByOrderId(id).stream()
                .map(it -> {
                    final Product product = productDao.findById(it.getProductId())
                            .orElseThrow(NoSuchElementException::new).toDomain();
                    return new OrderItem(product, it.getQuantity());
                }).collect(Collectors.toList());
    }
}
