package cart.domain;

import cart.exception.InvalidOrderOwnerException;
import cart.exception.InvalidOrderSizeException;

import java.util.List;

public class Order {

    private final Long id;
    private final OrderItems orderItems;
    private final long deliveryFee;
    private final MemberCoupon memberCoupon;
    private final Member member;

    public Order(final OrderItems orderItems, final long deliveryFee, final MemberCoupon memberCoupon, final Member member) {
        this(null, orderItems, deliveryFee, memberCoupon, member);
    }

    public Order(final Long id, final OrderItems orderItems, final long deliveryFee, final MemberCoupon memberCoupon, final Member member) {
        this.id = id;
        this.orderItems = orderItems;
        this.deliveryFee = deliveryFee;
        this.memberCoupon = memberCoupon;
        this.member = member;
    }

    public static Order createFromCartItems(final List<CartItem> cartItems, final long deliveryFee, final MemberCoupon memberCoupon, final Member member) {
        final OrderItems orderItems = OrderItems.from(cartItems);
        validateSize(orderItems);
        return new Order(orderItems, deliveryFee, memberCoupon, member);
    }

    private static void validateSize(final OrderItems orderItems) {
        if (orderItems.size() < 1) {
            throw new InvalidOrderSizeException();
        }
    }

    public TotalPrice calculateDiscountPrice() {
        final TotalPrice totalPrice = memberCoupon.calculatePrice(new TotalPrice(calculateOrderPrice(), deliveryFee));
        return memberCoupon.calculateDiscountPrice(totalPrice);
    }

    public long calculateOrderPrice() {
        return orderItems.sumPrice();
    }

    public void checkOwner(final Member member) {
        if (!this.member.equals(member)) {
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

    public Member getMember() {
        return member;
    }

    public MemberCoupon getMemberCoupon() {
        return memberCoupon;
    }
}
