package cart.domain.order;

import cart.domain.CartItem;
import cart.domain.member.Member;
import cart.domain.TotalPrice;
import cart.exception.InvalidOrderOwnerException;
import cart.exception.InvalidOrderSizeException;

import java.util.List;

public class Order {

    private final Long id;
    private final OrderItems orderItems;
    private final long deliveryFee;
    private final Long memberCouponId;
    private final Long memberId;

    public Order(final OrderItems orderItems, final long deliveryFee, final Long memberCouponId, final Long memberId) {
        this(null, orderItems, deliveryFee, memberCouponId, memberId);
    }

    public Order(final Long id, final OrderItems orderItems, final long deliveryFee, final Long memberCouponId, final Long memberId) {
        this.id = id;
        this.orderItems = orderItems;
        this.deliveryFee = deliveryFee;
        this.memberCouponId = memberCouponId;
        this.memberId = memberId;
    }

    public static Order createFromCartItems(final List<CartItem> cartItems, final long deliveryFee, final Long memberCouponId, final Long memberId) {
        final OrderItems orderItems = OrderItems.from(cartItems);
        validateSize(orderItems);
        return new Order(orderItems, deliveryFee, memberCouponId, memberId);
    }

    private static void validateSize(final OrderItems orderItems) {
        if (orderItems.size() < 1) {
            throw new InvalidOrderSizeException();
        }
    }

    public TotalPrice calculateTotalPrice() {
        return new TotalPrice(orderItems.sumPrice(), deliveryFee);
    }

    public void checkOwner(final Member member) {
        if (!this.memberId.equals(member.getId())) {
            throw new InvalidOrderOwnerException();
        }
    }

    public long getDeliveryFee() {
        return deliveryFee;
    }

    public Long getId() {
        return id;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getMemberCouponId() {
        return memberCouponId;
    }
}
