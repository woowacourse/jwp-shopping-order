package cart.domain.order;

import static java.util.stream.Collectors.toList;

import cart.domain.Member;
import cart.domain.coupon.MemberCoupon;
import cart.domain.product.CartItem;
import cart.domain.vo.Price;
import cart.exception.OrderNotOwnerException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Order {

    private final Long id;
    private final List<OrderProduct> orderProducts;
    private final LocalDateTime orderAt;
    private final Long memberId;
    private final MemberCoupon memberCoupon;

    public Order(final Long id, final List<OrderProduct> orderProducts, final LocalDateTime orderAt,
                 final Long memberId,
                 final MemberCoupon memberCoupon) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.orderAt = orderAt;
        this.memberId = memberId;
        this.memberCoupon = memberCoupon;
    }

    public Order(final Long id, final List<OrderProduct> orderProducts, final LocalDateTime orderAt,
                 final Long memberId) {
        this(id, orderProducts, orderAt, memberId, null);
    }

    public static Order of(final Member member, final List<CartItem> cartItems) {
        return of(member, cartItems, null);
    }

    public static Order of(final Member member, final List<CartItem> cartItems, final MemberCoupon coupon) {
        validateSameMember(member, cartItems);
        validateCheckOwner(member, coupon);
        final List<OrderProduct> orderProducts = cartItems.stream()
                .map(cartItem -> new OrderProduct(cartItem.getProduct(), cartItem.getQuantity()))
                .collect(toList());
        return new Order(null, orderProducts, LocalDateTime.now().withNano(0), member.getId(), coupon);
    }

    private static void validateCheckOwner(final Member member, final MemberCoupon coupon) {
        if (coupon != null) {
            coupon.checkOwner(member);
        }
    }

    private static void validateSameMember(final Member member, final List<CartItem> cartItems) {
        for (final CartItem cartItem : cartItems) {
            cartItem.checkOwner(member);
        }
    }

    public Price getOriginPrice() {
        return orderProducts.stream()
                .map(OrderProduct::calculateTotalPrice)
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

    public LocalDateTime getOrderAt() {
        return orderAt;
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

    public void checkOwner(final Member member) {
        if (!member.getId().equals(memberId)) {
            throw new OrderNotOwnerException();
        }
    }
}
