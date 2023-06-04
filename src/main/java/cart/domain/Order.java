package cart.domain;

import static java.util.stream.Collectors.toList;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.exception.ExceptionType;
import cart.exception.OrderException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Order {

    private static final DateTimeFormatter ORDER_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private final Long id;
    private final Member member;
    private final List<Item> items;
    private final Money deliveryFee;
    private final LocalDateTime orderDate;
    private final String orderNumber;
    private final Coupon coupon;

    public Order(Member member, List<Item> items, Money deliveryFee, Coupon coupon) {
        this.id = null;
        this.member = member;
        this.items = items;
        this.deliveryFee = deliveryFee;
        this.orderDate = LocalDateTime.now();
        this.orderNumber = createOrderNumber(member.getId());
        this.coupon = coupon;
    }

    public Order(Long id, Member member, List<Item> items, Money deliveryFee, LocalDateTime orderDate,
                 String orderNumber, Coupon coupon) {
        this.id = id;
        this.member = member;
        this.items = items;
        this.deliveryFee = deliveryFee;
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
        this.coupon = coupon;
    }

    public static Order of(Member member, List<CartItem> cartItems, int deliveryFee, MemberCoupon memberCoupon) {
        memberCoupon.check(member);
        validateOwner(member, cartItems);
        List<Item> items = cartItems.stream()
                .map(CartItem::getItem)
                .collect(toList());
        return new Order(member, items, new Money(deliveryFee), memberCoupon.getCoupon());
    }

    private static void validateOwner(Member member, List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            cartItem.validateOwner(member);
        }
    }

    private String createOrderNumber(Long memberId) {
        return orderDate.format(ORDER_NUMBER_FORMAT) + memberId;
    }

    public Money calculateTotalPrice() {
        Money totalCartsPrice = calculateBeforeDiscountPrice();
        Money discountedPrice = coupon.discountPrice(totalCartsPrice);
        return discountedPrice.add(deliveryFee);
    }

    public Money calculateBeforeDiscountPrice() {
        return items.stream()
                .map(Item::calculateItemPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public Money calculateDiscountPrice() {
        Money beforeDiscountPrice = calculateBeforeDiscountPrice();
        return beforeDiscountPrice.subtract(coupon.discountPrice(beforeDiscountPrice).getValue());
    }

    public void validateOwner(Member member) {
        if (!this.member.equals(member)) {
            throw new OrderException(ExceptionType.NO_AUTHORITY_ORDER);
        }
    }

    public void validateOrderPrice(BigDecimal totalOrderPrice) {
        Money totalPrice = calculateTotalPrice();
        if (totalPrice.isNotSameValue(totalOrderPrice)) {
            throw new OrderException(ExceptionType.INCORRECT_PRICE);
        }
    }

    public boolean isUnusedCoupon() {
        return !coupon.isCoupon();
    }

    public Long getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public Member getMember() {
        return member;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
