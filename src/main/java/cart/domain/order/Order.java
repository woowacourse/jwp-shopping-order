package cart.domain.order;

import static cart.exception.badrequest.BadRequestErrorType.COUPON_UNAVAILABLE;

import cart.domain.Member;
import cart.domain.cart.CartItems;
import cart.domain.coupon.MemberCoupon;
import cart.exception.badrequest.BadRequestException;
import cart.exception.forbidden.ForbiddenException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Order {

    private final Long id;
    private final Member member;
    private final MemberCoupon memberCoupon;
    private final List<OrderItem> orderItems;
    private final ShippingFee shippingFee;
    private final int totalOrderPrice;
    private final LocalDateTime createdAt;

    public Order(final Member member, final MemberCoupon memberCoupon, final List<OrderItem> orderItems,
            final ShippingFee shippingFee, final int totalOrderPrice) {
        this(null, member, memberCoupon, orderItems, shippingFee, totalOrderPrice, null);
    }

    public Order(final Long id, final Member member, final MemberCoupon memberCoupon, final List<OrderItem> orderItems,
            final ShippingFee shippingFee, final int totalOrderPrice, final LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.memberCoupon = memberCoupon;
        this.orderItems = orderItems;
        this.shippingFee = shippingFee;
        this.totalOrderPrice = totalOrderPrice;
        this.createdAt = createdAt;
    }

    public static Order of(final Member member, final CartItems cartItems, final MemberCoupon memberCoupon) {
        validateCartItems(member, cartItems);
        validateCoupon(member, cartItems, memberCoupon);

        List<OrderItem> orderItems = cartItems.getCartItems().stream()
                .map(OrderItem::of)
                .collect(Collectors.toList());

        int discountPrice = memberCoupon.getDiscountPrice(cartItems);
        int totalProductPrice = cartItems.calculateTotalProductPrice();
        ShippingFee shippingFee = ShippingFee.fromTotalOrderPrice(totalProductPrice - discountPrice);
        int totalOrderPrice = totalProductPrice - discountPrice + shippingFee.getValue();

        return new Order(member, memberCoupon, orderItems, shippingFee, totalOrderPrice);
    }

    private static void validateCartItems(final Member member, final CartItems cartItems) {
        cartItems.checkNotEmpty();
        cartItems.checkOwner(member);
    }

    private static void validateCoupon(final Member member, final CartItems cartItems,
            final MemberCoupon memberCoupon) {
        memberCoupon.checkOwner(member);
        if (!memberCoupon.isApplicable(cartItems)) {
            throw new BadRequestException(COUPON_UNAVAILABLE);
        }
    }

    public int calculateTotalProductPrice() {
        return orderItems.stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public int calculateDiscountPrice() {
        return calculateTotalProductPrice() + shippingFee.getValue() - totalOrderPrice;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new ForbiddenException();
        }
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

    public int getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
