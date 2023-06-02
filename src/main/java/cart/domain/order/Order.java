package cart.domain.order;

import cart.domain.Member;
import cart.domain.cart.CartItems;
import cart.domain.coupon.MemberCoupon;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Order {

    private final Long id;
    private final Member member;
    private final MemberCoupon memberCoupon;
    private final List<OrderItem> orderItems;
    private final ShippingFee shippingFee;
    private final int totalOrderAmount;
    private final LocalDateTime createdAt;

    public Order(final Member member, final MemberCoupon memberCoupon, final List<OrderItem> orderItems,
            final ShippingFee shippingFee, final int totalOrderAmount) {
        this(null, member, memberCoupon, orderItems, shippingFee, totalOrderAmount, null);
    }

    public Order(final Long id, final Member member, final MemberCoupon memberCoupon, final List<OrderItem> orderItems,
            final ShippingFee shippingFee, final int totalOrderAmount, final LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.memberCoupon = memberCoupon;
        this.orderItems = orderItems;
        this.shippingFee = shippingFee;
        this.totalOrderAmount = totalOrderAmount;
        this.createdAt = createdAt;
    }

    public static Order order(final Member member, final CartItems cartItems, final MemberCoupon memberCoupon) {
        List<OrderItem> orderItems = cartItems.getCartItems().stream()
                .map(OrderItem::of)
                .collect(Collectors.toList());

        int totalOrderAmount = calculateTotalOrderAmount(cartItems, memberCoupon);
        ShippingFee shippingFee = ShippingFee.fromTotalOrderAmount(totalOrderAmount);

        return new Order(member, memberCoupon, orderItems, shippingFee, totalOrderAmount);
    }

    private static int calculateTotalOrderAmount(final CartItems cartItems, final MemberCoupon coupon) {
        int discountPrice = coupon.getDiscountPrice(cartItems);
        int totalProductPrice = cartItems.getTotalProductPrice();
        return totalProductPrice - discountPrice;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public MemberCoupon getMemberCoupon() {
        return memberCoupon;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public ShippingFee getShippingFee() {
        return shippingFee;
    }

    public int getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
