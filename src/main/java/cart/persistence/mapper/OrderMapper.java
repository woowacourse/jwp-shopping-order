package cart.persistence.mapper;

import cart.domain.Member;
import cart.domain.coupon.MemberCoupon;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.ShippingFee;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.OrderItemEntity;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
    }

    public static Order toDomain(final OrderEntity orderEntity, final MemberEntity memberEntity, final
    MemberCouponEntity memberCouponEntity, final CouponEntity couponEntity,
            final List<OrderItemEntity> orderItemEntities) {
        Member member = memberEntity.toDomain();
        MemberCoupon memberCoupon = MemberCouponMapper.toDomain(memberCouponEntity, couponEntity, memberEntity);
        List<OrderItem> orderItems = orderItemEntities.stream()
                .map(OrderItemEntity::toDomain)
                .collect(Collectors.toList());
        return new Order(orderEntity.getId(), member, memberCoupon, orderItems,
                new ShippingFee(orderEntity.getShippingFee()),
                orderEntity.getTotalPrice(), orderEntity.getCreatedAt().toLocalDateTime());
    }
}
