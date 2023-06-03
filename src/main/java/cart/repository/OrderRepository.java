package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.domain.Item;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderProductEntity;
import cart.exception.NonExistCouponException;
import cart.exception.NonExistMemberException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;
    private final MemberDao memberDao;
    private final CouponDao couponDao;

    public OrderRepository(OrderDao orderDao, OrderProductDao orderProductDao, MemberDao memberDao,
                           CouponDao couponDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.memberDao = memberDao;
        this.couponDao = couponDao;
    }

    public Order save(Order order) {
        Member member = order.getMember();
        Money deliveryFee = order.getDeliveryFee();
        Long savedOrderId = orderDao.save(toEntity(order));

        List<OrderProductEntity> orderProductEntities = toEntities(order, savedOrderId);
        orderProductDao.saveAll(orderProductEntities);
        return new Order(savedOrderId, member, order.getItems(), deliveryFee, order.getOrderDate(),
                order.getOrderNumber(), order.getCoupon());
    }

    private OrderEntity toEntity(Order order) {
        Member member = order.getMember();
        Money deliveryFee = order.getDeliveryFee();
        Long couponId = order.getCoupon().getId();
        return new OrderEntity(member.getId(), couponId, deliveryFee.getValue().intValue(), order.getOrderNumber(),
                order.getOrderDate());
    }

    private List<OrderProductEntity> toEntities(Order order, Long savedOrderId) {
        return order.getItems().stream()
                .map(item -> toEntity(savedOrderId, item))
                .collect(Collectors.toList());
    }

    private OrderProductEntity toEntity(Long savedOrderId, Item item) {
        int quantity = item.getQuantity();
        Product product = item.getProduct();
        return new OrderProductEntity(
                savedOrderId,
                product.getId(),
                quantity,
                product.getName(),
                product.getPrice().getValue(),
                product.getImageUrl()
        );
    }

    public Optional<Order> findById(Long id) {
        Optional<OrderEntity> savedOrderEntity = orderDao.findById(id);
        if (savedOrderEntity.isEmpty()) {
            return Optional.empty();
        }
        OrderEntity orderEntity = savedOrderEntity.get();
        Member member = getMember(orderEntity);
        Order order = toOrder(orderEntity, member);
        return Optional.of(order);
    }

    public List<Order> findAllByMember(Member member) {
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(member.getId());
        return orderEntities.stream()
                .map(orderEntity -> toOrder(orderEntity, member))
                .collect(Collectors.toList());
    }

    private Order toOrder(OrderEntity orderEntity, Member member) {
        List<OrderProductEntity> orderProductEntities = orderProductDao.findByOrderId(orderEntity.getId());
        List<Item> items = toItems(orderProductEntities);
        Coupon coupon = toCoupon(orderEntity.getCouponId());
        return new Order(
                orderEntity.getId(),
                member,
                items,
                new Money(orderEntity.getDeliveryFee()),
                orderEntity.getCreatedAt(),
                orderEntity.getOrderNumber(),
                coupon
        );
    }

    private Coupon toCoupon(Long couponId) {
        if (couponId == 0) {
            return Coupon.NONE;
        }
        return couponDao.findById(couponId)
                .map(CouponEntity::toDomain)
                .orElseThrow(NonExistCouponException::new);
    }

    private Member getMember(OrderEntity order) {
        return memberDao.findById(order.getMemberId())
                .orElseThrow(NonExistMemberException::new)
                .toDomain();
    }

    private List<Item> toItems(List<OrderProductEntity> orderProductEntities) {
        return orderProductEntities.stream()
                .map(this::toItem)
                .collect(Collectors.toList());
    }

    private Item toItem(OrderProductEntity orderProduct) {
        return new Item(
                new Product(
                        orderProduct.getProductId(),
                        orderProduct.getProductName(),
                        orderProduct.getProductPrice(),
                        orderProduct.getProductImageUrl()
                )
        );
    }
}
