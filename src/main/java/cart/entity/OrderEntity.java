package cart.entity;

import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.vo.Amount;
import java.util.List;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final int totalAmount;
    private final int discountedAmount;
    private final int deliveryAmount;
    private final String address;

    public OrderEntity(final Long id, final Long memberId, final Long couponId, final int totalAmount,
                       final int discountedAmount, final int deliveryAmount,
                       final String address) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.totalAmount = totalAmount;
        this.discountedAmount = discountedAmount;
        this.deliveryAmount = deliveryAmount;
        this.address = address;
    }

    public static OrderEntity of(final Order order, final Long memberId) {
        return new OrderEntity(order.getId(), memberId, order.getCoupon().getCoupon().getId(),
                order.getTotalProductAmount().getValue(), order.discountProductAmount().getValue(),
                order.getDeliveryAmount().getValue(), order.getAddress());
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getDiscountedAmount() {
        return discountedAmount;
    }

    public int getDeliveryAmount() {
        return deliveryAmount;
    }

    public String getAddress() {
        return address;
    }

    public Order toDomain(final List<OrderItem> orderItems, final MemberCoupon memberCoupon) {
        return new Order(id, orderItems, memberCoupon, Amount.of(totalAmount), Amount.of(deliveryAmount), address);
    }
}
