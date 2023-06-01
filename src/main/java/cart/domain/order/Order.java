package cart.domain.order;

import static java.util.stream.Collectors.toList;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.product.CartItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Order {

    private final Long id;
    private final List<OrderProduct> orderProducts;
    private final LocalDateTime timeStamp;
    private final Member member;
    private final Coupon coupon;

    public Order(final Long id, final List<OrderProduct> orderProducts, final LocalDateTime timeStamp,
                 final Member member, final Coupon coupon) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.timeStamp = timeStamp;
        this.member = member;
        this.coupon = coupon;
    }

    public static Order of(final Member member, final List<CartItem> cartItems, final Coupon coupon) {
        validateSameMember(member, cartItems);
        List<OrderProduct> orderProducts = cartItems.stream()
                .map(cartItem -> new OrderProduct(cartItem.getProduct(), cartItem.getQuantity()))
                .collect(toList());
        return new Order(null, orderProducts, LocalDateTime.now().withNano(0), member, coupon);
    }

    public static Order of(final Member member, final List<CartItem> cartItems) {
        return of(member, cartItems, null);
    }

    private static void validateSameMember(final Member member, final List<CartItem> cartItems) {
        if (isCartItemsNotMatchMember(member, cartItems)) {
            throw new IllegalArgumentException("장바구니 품목을 담은 멤버와 주문 멤버가 동일하지 않습니다.");
        }
    }

    private static boolean isCartItemsNotMatchMember(final Member member, final List<CartItem> cartItems) {
        return !cartItems.stream()
                .allMatch(cartItem -> cartItem.getMember().equals(member));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public Optional<Coupon> getCoupon() {
        return Optional.ofNullable(coupon);
    }
}
