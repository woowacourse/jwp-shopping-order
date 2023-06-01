package cart.persistence.dto;

import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.OrderEntity;

public class OrderDetailDTO {

    private final OrderEntity orderEntity;
    private final MemberEntity memberEntity;
    private final MemberCouponEntity memberCouponEntity;

    public OrderDetailDTO(final OrderEntity orderEntity, final MemberEntity memberEntity,
            final MemberCouponEntity memberCouponEntity) {
        this.orderEntity = orderEntity;
        this.memberEntity = memberEntity;
        this.memberCouponEntity = memberCouponEntity;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public MemberCouponEntity getMemberCouponEntity() {
        return memberCouponEntity;
    }
}
