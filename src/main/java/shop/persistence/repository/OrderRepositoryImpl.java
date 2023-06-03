package shop.persistence.repository;

import org.springframework.stereotype.Repository;
import shop.domain.cart.Quantity;
import shop.domain.coupon.Coupon;
import shop.domain.member.Member;
import shop.domain.order.Order;
import shop.domain.order.OrderDetail;
import shop.domain.order.OrderItem;
import shop.domain.order.OrderPrice;
import shop.domain.product.Product;
import shop.domain.repository.OrderRepository;
import shop.persistence.dao.OrderCouponDao;
import shop.persistence.dao.OrderDao;
import shop.persistence.dao.OrderProductDao;
import shop.persistence.entity.OrderCouponEntity;
import shop.persistence.entity.OrderEntity;
import shop.persistence.entity.OrderProductEntity;
import shop.persistence.entity.detail.OrderCouponDetail;
import shop.persistence.entity.detail.OrderProductDetail;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;
    private final OrderCouponDao orderCouponDao;

    public OrderRepositoryImpl(OrderDao orderDao, OrderProductDao orderProductDao,
                               OrderCouponDao orderCouponDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.orderCouponDao = orderCouponDao;
    }

    @Override
    public Long save(Long memberId, Long couponId, Order order) {
        Long savedOrderId = saveOrder(memberId, order);
        saveOrderProduct(savedOrderId, order);
        saveOrderCoupon(savedOrderId, couponId);

        return savedOrderId;
    }

    private void saveOrderCoupon(Long orderId, Long couponId) {
        if (couponId == null) {
            return;
        }

        OrderCouponEntity orderCouponEntity = new OrderCouponEntity(orderId, couponId);
        orderCouponDao.insert(orderCouponEntity);
    }

    private void saveOrderProduct(Long orderId, Order order) {
        List<OrderProductEntity> orderProductEntities = order.getOrderItems().stream()
                .map(orderItem -> createOrderProductEntity(orderId, orderItem))
                .collect(Collectors.toList());

        orderProductDao.insertAll(orderProductEntities);
    }

    private OrderProductEntity createOrderProductEntity(Long orderId, OrderItem orderItem) {
        return new OrderProductEntity(
                orderId,
                orderItem.getProduct().getId(),
                orderItem.getProduct().getPrice(),
                orderItem.getQuantity()
        );
    }

    private Long saveOrder(Long memberId, Order order) {
        OrderEntity orderEntity = new OrderEntity(
                memberId,
                order.getTotalPrice(),
                order.getDiscountedPrice(),
                order.getDeliveryPrice(),
                order.getOrderedAt()
        );

        return orderDao.insert(orderEntity);
    }

    @Override
    public OrderDetail findDetailsByMemberAndOrderId(Member member, Long orderId) {
        OrderEntity orderEntity = orderDao.findById(orderId);

        List<OrderProductDetail> orderProducts = orderProductDao.findAllByOrderId(orderId);
        Optional<OrderCouponDetail> orderCouponDetail = orderCouponDao.findCouponByOrderId(orderId);
        Order order = toOrder(member, orderEntity, orderProducts);

        return toOrderDetail(orderCouponDetail, order);
    }


    private Order toOrder(Member member, OrderEntity orderEntity, List<OrderProductDetail> orderProducts) {
        Long orderId = orderEntity.getId();
        List<OrderItem> orderItems = toOrderItems(member, orderProducts);
        OrderPrice orderPrice = toOrderPrice(orderEntity);
        LocalDateTime orderedAt = orderEntity.getOrderAt();

        return new Order(orderId, orderItems, orderPrice, orderedAt);
    }

    private List<OrderItem> toOrderItems(Member member, List<OrderProductDetail> orderProducts) {
        return orderProducts.stream()
                .map(orderProduct -> toOrderItem(member, orderProduct))
                .collect(Collectors.toList());
    }

    private OrderItem toOrderItem(Member member, OrderProductDetail orderProductDetail) {
        return new OrderItem(
                orderProductDetail.getId(),
                toProduct(orderProductDetail),
                new Quantity(orderProductDetail.getQuantity()),
                member
        );
    }

    private Product toProduct(OrderProductDetail orderProductDetail) {
        return new Product(
                orderProductDetail.getProductId(),
                orderProductDetail.getProductName(),
                orderProductDetail.getOrderedProductPrice(),
                orderProductDetail.getImageUrl()
        );
    }

    private OrderPrice toOrderPrice(OrderEntity orderEntity) {
        return OrderPrice.create(
                orderEntity.getTotalPrice(),
                orderEntity.getDeliveryPrice(),
                orderEntity.getDiscountedTotalPrice()
        );
    }

    private OrderDetail toOrderDetail(Optional<OrderCouponDetail> orderCouponDetail, Order order) {
        if (orderCouponDetail.isEmpty()) {
            return new OrderDetail(order, null);
        }

        Coupon coupon = toCoupon(orderCouponDetail.get());

        return new OrderDetail(order, coupon);
    }

    private Coupon toCoupon(OrderCouponDetail orderCouponDetail) {
        return new Coupon(
                orderCouponDetail.getCouponId(),
                orderCouponDetail.getName(),
                orderCouponDetail.getDiscountRate(),
                orderCouponDetail.getPeriod(),
                orderCouponDetail.getExpiredAt()
        );
    }

    @Override
    public List<Order> findAllByMember(Member member) {
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(member.getId());

        return toOrders(member, orderEntities);
    }

    private List<Order> toOrders(Member member, List<OrderEntity> orderEntities) {
        List<Long> orderIds = getOrderIds(orderEntities);
        Map<Long, List<OrderProductDetail>> orderProductDetailsById
                = getAllOrderProductDetails(orderIds);

        return orderIds.stream()
                .map(orderId -> {
                    OrderEntity orderEntity = getOrderEntityById(orderEntities, orderId);
                    List<OrderProductDetail> orderProductDetail = orderProductDetailsById.get(orderId);
                    return toOrder(member, orderEntity, orderProductDetail);
                })
                .collect(Collectors.toList());
    }

    private OrderEntity getOrderEntityById(List<OrderEntity> orderEntities, Long orderId) {
        return orderEntities.stream()
                .filter(entity -> Objects.equals(entity.getId(), orderId))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private Map<Long, List<OrderProductDetail>> getAllOrderProductDetails(List<Long> orderIds) {
        return orderProductDao.findAllByOrderIds(orderIds).stream()
                .collect(Collectors.groupingBy(OrderProductDetail::getOrderId));
    }

    private List<Long> getOrderIds(List<OrderEntity> orderEntities) {
        return orderEntities.stream()
                .map(OrderEntity::getId)
                .collect(Collectors.toList());
    }
}
