package cart.db.mapper;

import cart.db.entity.*;
import cart.domain.product.Item;
import cart.domain.product.Items;
import cart.domain.product.Product;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.order.Order;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toDomain(
            final OrderMemberDetailEntity orderMember,
            final List<OrderProductDetailEntity> orderProducts
    ) {
        Member member = new Member(orderMember.getMemberId(), orderMember.getName(), orderMember.getPassword());
        List<Item> items = orderProducts.stream().map(entity ->
                new Item(
                        entity.getId(),
                        new Product(entity.getProductId(), entity.getProductName(), entity.getProductPrice(), entity.getProductImageUrl(), entity.getProductIsDeleted()),
                        entity.getQuantity()
                )
        ).collect(Collectors.toList());
        return new Order(orderMember.getId(), member, items, null, orderMember.getTotalPrice(), orderMember.getDeliveryPrice(), orderMember.getOrderedAt());
    }

    public static Order toDomain(
            final OrderMemberDetailEntity orderMember,
            final List<OrderProductDetailEntity> orderProducts,
            final OrderCouponDetailEntity orderCoupon
    ) {
        Member member = new Member(orderMember.getMemberId(), orderMember.getName(), orderMember.getPassword());
        Coupon coupon = new Coupon(orderCoupon.getCouponId(), orderCoupon.getCouponName(), orderCoupon.getDiscountRate(), orderCoupon.getPeriod(), orderCoupon.getExpiredAt());
        List<Item> items = orderProducts.stream().map(entity ->
                new Item(
                        entity.getId(),
                        new Product(entity.getProductId(), entity.getProductName(), entity.getProductPrice(), entity.getProductImageUrl(), entity.getProductIsDeleted()),
                        entity.getQuantity()
                )
        ).collect(Collectors.toList());
        return new Order(orderMember.getId(), member, items, coupon, orderMember.getTotalPrice(), orderMember.getDeliveryPrice(), orderMember.getOrderedAt());
    }

    public static List<Order> toDomain(
            final List<OrderMemberDetailEntity> orders,
            final List<OrderProductDetailEntity> orderProducts,
            final List<OrderCouponDetailEntity> orderCoupons
    ) {
        Map<Long, OrderMemberDetailEntity> ordersMap = orders.stream()
                .collect(Collectors.toMap(OrderMemberDetailEntity::getId, Function.identity()));

        Map<Long, List<Item>> orderProductsMap = orderProducts.stream()
                .collect(Collectors.groupingBy(
                        OrderProductDetailEntity::getOrderId,
                        Collectors.mapping(
                                entity -> new Item(
                                        entity.getId(),
                                        new Product(entity.getProductId(), entity.getProductName(), entity.getProductPrice(), entity.getProductImageUrl(), entity.getProductIsDeleted()),
                                        entity.getQuantity()
                                ),
                                Collectors.toList()
                        )
                ));

        Map<Long, Coupon> orderCouponsMap = orderCoupons.stream()
                .collect(Collectors.toMap(
                        OrderCouponDetailEntity::getOrderId,
                        entity -> new Coupon(entity.getCouponId(), entity.getCouponName(), entity.getDiscountRate(), entity.getPeriod(), entity.getExpiredAt())
                ));

        return ordersMap.keySet().stream().map(orderId -> {
            OrderMemberDetailEntity entity = ordersMap.get(orderId);
            return new Order(
                    entity.getId(),
                    new Member(entity.getMemberId(), entity.getName(), entity.getPassword()),
                    orderProductsMap.get(orderId),
                    orderCouponsMap.get(orderId),
                    entity.getTotalPrice(),
                    entity.getDeliveryPrice(),
                    entity.getOrderedAt()
            );
        }).collect(Collectors.toList());
    }

    public static OrderEntity toEntity(final Order order) {
        return new OrderEntity(
                order.getId(),
                order.getMember().getId(),
                order.getTotalPrice(),
                order.getDiscountedTotalPrice(),
                order.getDeliveryPrice(),
                order.getOrderedAt()
        );
    }

    public static List<OrderProductEntity> toEntity(final Long orderId, final Items items) {
        return items.getItems()
                .stream()
                .map(item -> toEntity(orderId, item))
                .collect(Collectors.toList());
    }

    private static OrderProductEntity toEntity(final Long orderId, final Item item) {
        return new OrderProductEntity(
                orderId,
                item.getProduct().getId(),
                item.getProduct().getPrice(),
                item.getQuantity()
        );
    }

    public static OrderCouponEntity toEntity(final Long orderId, final Coupon coupon) {
        return new OrderCouponEntity(
                orderId,
                coupon.getId()
        );
    }
}
