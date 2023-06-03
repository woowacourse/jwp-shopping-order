package cart.domain.order;

import cart.domain.Member;
import cart.domain.Money;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.MemberCoupons;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import cart.domain.product.CartItem;
import cart.domain.product.OrderItem;
import cart.exception.OrderException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Order {

    private Long id;
    private final Money originalTotalItemPrice;
    private final Money actualTotalItemPrice;
    private final MemberCoupons appliedCoupons;
    private final List<OrderItem> orderItems;
    private final Money deliveryFee;
    private final Member member;

    private Order(
            Long id,
            Money originalTotalItemPrice,
            Money actualTotalItemPrice,
            MemberCoupons appliedCoupons,
            List<OrderItem> orderItems,
            Money deliveryFee,
            Member member
    ) {
        this.id = id;
        this.originalTotalItemPrice = originalTotalItemPrice;
        this.actualTotalItemPrice = actualTotalItemPrice;
        this.appliedCoupons = appliedCoupons;
        this.orderItems = orderItems;
        this.deliveryFee = deliveryFee;
        this.member = member;
    }

    private Order(Money originalTotalItemPrice,
                  Money actualTotalItemPrice,
                  MemberCoupons appliedCoupons,
                  List<OrderItem> orderItems,
                  Money deliveryFee,
                  Member member
    ) {
        this.appliedCoupons = appliedCoupons;
        this.id = null;
        this.originalTotalItemPrice = originalTotalItemPrice;
        this.actualTotalItemPrice = actualTotalItemPrice;
        this.orderItems = orderItems;
        this.deliveryFee = deliveryFee;
        this.member = member;
    }

    public static Order make(
            List<CartItem> cartItems,
            List<MemberCoupon> coupons,
            Money deliveryFee,
            Member member,
            DiscountPolicyProvider discountPolicyProvider
    ) {
        validateCartItems(cartItems);

        Money totalPrice = calculateTotlaPrice(cartItems);
        MemberCoupons memberCoupons = MemberCoupons.of(coupons, discountPolicyProvider);
        Money actualPrice = memberCoupons.apply(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(OrderItem::of)
                .collect(Collectors.toList());

        return new Order(totalPrice, actualPrice, memberCoupons, orderItems, deliveryFee, member);
    }

    private static void validateCartItems(final List<CartItem> cartItems) {
        if (cartItems.size() == 0) {
            throw new OrderException.NoCartItem();
        }
    }

    private static Money calculateTotlaPrice(final List<CartItem> cartItems) {
        Money totalPrice = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(Money::plus)
                .orElse(new Money(0));
        return totalPrice;
    }

    public final void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new OrderException.IllegalMember(this, member);
        }
    }

    public Long getId() {
        return id;
    }

    public Money getOriginalTotalItemPrice() {
        return originalTotalItemPrice;
    }

    public Money getActualTotalItemPrice() {
        return actualTotalItemPrice;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public Member getMember() {
        return member;
    }
}
