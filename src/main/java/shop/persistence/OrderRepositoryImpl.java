package shop.persistence;

import org.springframework.stereotype.Repository;
import shop.application.order.dto.OrderDetailDto;
import shop.domain.cart.Quantity;
import shop.domain.coupon.Coupon;
import shop.domain.member.Member;
import shop.domain.order.Order;
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
        saveOrderProduct(order);
        saveOrderCoupon(memberId, couponId);

        return savedOrderId;
    }

    private void saveOrderCoupon(Long memberId, Long couponId) {
        if (couponId == null) {
            return;
        }

        OrderCouponEntity orderCouponEntity = new OrderCouponEntity(memberId, couponId);
        orderCouponDao.insert(orderCouponEntity);
    }

    private void saveOrderProduct(Order order) {
        List<OrderProductEntity> orderProductEntities = order.getOrderItems().stream()
                .map(orderItem -> createOrderProductEntity(order.getId(), orderItem))
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
                order.getOrderPrice(),
                order.getDeliveryPrice(),
                order.getOrderedAt()
        );

        return orderDao.insert(orderEntity);
    }

    @Override
    public OrderDetailDto findDetailsByMemberAndOrderId(Member member, Long orderId) {
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
        LocalDateTime orderedAt = orderEntity.getOrderedAt();

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
                orderEntity.getTotalProductPrice(),
                orderEntity.getDeliveryPrice()
        );
    }

    private OrderDetailDto toOrderDetail(Optional<OrderCouponDetail> orderCouponDetail, Order order) {
        if (orderCouponDetail.isEmpty()) {
            return new OrderDetailDto(order, null);
        }

        Coupon coupon = toCoupon(orderCouponDetail.get());

        return new OrderDetailDto(order, coupon);
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

        List<Long> orderIds = orderEntities.stream()
                .map(OrderEntity::getId)
                .collect(Collectors.toList());

        Map<Long, List<OrderProductDetail>> orderProductDetailsByOrderId =
                orderProductDao.findAllByOrderIds(orderIds).stream()
                        .collect(Collectors.groupingBy(OrderProductDetail::getOrderId));

        List<Order> orders = new ArrayList<>();
        for (Long orderId : orderIds) {
            OrderEntity orderEntity = orderEntities.stream()
                    .filter(entity -> Objects.equals(entity.getId(), orderId))
                    .findFirst()
                    .get();
            // TODO: 2023-06-01  너무나 자명해서 예외를 던져야하나 ..?

            List<OrderProductDetail> orderProductDetails = orderProductDetailsByOrderId.get(orderId);

            orders.add(toOrder(member, orderEntity, orderProductDetails));
        }

        return orders;
    }
}
