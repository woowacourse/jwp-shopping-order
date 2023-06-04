package cart.domain;

import static java.util.stream.Collectors.toList;

import cart.domain.util.DiscountCalculator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Order {

    private final Long id;
    private final LocalDateTime timeStamp;
    private final Member member;
    private final Coupon coupon;
    private final List<OrderProduct> orderProducts;

    public Order(final Long id,
                 final LocalDateTime timeStamp,
                 final Member member,
                 final Coupon coupon,
                 final List<OrderProduct> orderProducts) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.member = member;
        this.coupon = coupon;
        this.orderProducts = orderProducts;
    }

    public static Order of(final Member member,
                           final Coupon coupon,
                           final List<CartItem> cartItems) {
        validateSameMember(member, cartItems);

        List<OrderProduct> orderProducts = cartItems.stream()
                .map(Order::createOrderProductFromCartItem)
                .collect(toList());

        return new Order(null, LocalDateTime.now(), member, coupon, orderProducts);
    }

    private static OrderProduct createOrderProductFromCartItem(final CartItem cartItem) {
        return new OrderProduct(
                cartItem.getProduct(),
                Quantity.from(cartItem.getQuantity())
        );
    }

    private static void validateSameMember(final Member member,
                                           final List<CartItem> cartItems) {
        if (isCartItemsNotMatchMember(member, cartItems)) {
            throw new IllegalArgumentException("장바구니 품목을 담은 멤버와 주문 멤버가 동일하지 않습니다.");
        }
    }

    private static boolean isCartItemsNotMatchMember(final Member member,
                                                     final List<CartItem> cartItems) {
        return !cartItems.stream()
                .allMatch(cartItem -> cartItem.getMember().equals(member));
    }

    public Integer calculateTotalPrice() {
        return orderProducts.stream()
                .mapToInt(this::calculateOrderProduct)
                .sum();
    }

    public Integer calculateCutPrice() {
        Integer originPrice = calculateTotalPrice();
        Integer priceAfterDiscount = DiscountCalculator.calculatePriceAfterDiscount(originPrice, getOptionalCoupon());
        return  originPrice - priceAfterDiscount;
    }

    private int calculateOrderProduct(final OrderProduct orderProduct) {
        return orderProduct.getProduct().getPrice() *
                orderProduct.getQuantity().getValue();
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

    public Optional<Coupon> getOptionalCoupon() {
        return Optional.ofNullable(coupon);
    }

    public String getCouponName() {
        if (coupon == null) {
            return "적용된 쿠폰이 없습니다.";
        }

        return coupon.getName();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", timeStamp=" + timeStamp +
                ", member=" + member +
                ", coupon=" + coupon +
                ", orderProducts=" + orderProducts +
                '}';
    }

}
