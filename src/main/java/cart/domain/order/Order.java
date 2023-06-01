package cart.domain.order;

import static java.util.stream.Collectors.toList;

import cart.domain.Member;
import cart.domain.coupon.MemberCoupon;
import cart.domain.product.CartItem;
import cart.domain.vo.Price;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Order {

    private final Long id;
    private final List<OrderProduct> orderProducts;
    private final LocalDateTime timeStamp;
    private final Long memberId;
    private final MemberCoupon memberCoupon;

    public Order(final Long id, final List<OrderProduct> orderProducts, final LocalDateTime timeStamp,
                 final Long memberId,
                 final MemberCoupon memberCoupon) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.timeStamp = timeStamp;
        this.memberId = memberId;
        this.memberCoupon = memberCoupon;
    }

    public Order(final Long id, final List<OrderProduct> orderProducts, final LocalDateTime timeStamp,
                 final Long memberId) {
        this(id, orderProducts, timeStamp, memberId, null);
    }

    public static Order of(final Member member, final List<CartItem> cartItems, final MemberCoupon coupon) {
        validateSameMember(member, cartItems);
        final List<OrderProduct> orderProducts = cartItems.stream()
                .map(cartItem -> new OrderProduct(cartItem.getProduct(), cartItem.getQuantity()))
                .collect(toList());
        return new Order(null, orderProducts, LocalDateTime.now().withNano(0), member.getId(), coupon);
    }

    public static Order of(final Member member, final List<CartItem> cartItems) {
        return of(member, cartItems, null);
    }

    private static void validateSameMember(final Member member, final List<CartItem> cartItems) {
        for (final CartItem cartItem : cartItems) {
            cartItem.checkOwner(member);
        }
    }

    public Price getOriginPrice() {
        return orderProducts.stream()
                .map(OrderProduct::getTotalPrice)
                .reduce(new Price(0), Price::sum);
    }

    public Price getDiscountPrice() {
        if (memberCoupon == null) {
            return new Price(0);
        }
        return memberCoupon.discount(getOriginPrice());
    }

    public Price getTotalPrice() {
        return getOriginPrice().subtract(getDiscountPrice());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public Optional<MemberCoupon> getMemberCoupon() {
        return Optional.ofNullable(memberCoupon);
    }
}
