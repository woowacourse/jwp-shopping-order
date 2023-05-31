package cart.domain2.cart;

import cart.domain2.common.Money;
import cart.exception.InvalidOrderException;
import java.util.List;
import java.util.Objects;

public class Order {

    private static final Money DEFAULT_DELIVERY_FEE = Money.from(3000L);

    private final Long id;
    private final MemberCoupon memberCoupon;
    private final Long memberId;
    private final Money deliveryFee;
    private final List<Item> items;

    public Order(final MemberCoupon memberCoupon, final Long memberId, final List<Item> items) {
        this(null, memberCoupon, memberId, DEFAULT_DELIVERY_FEE, items);
    }

    public Order(
            final Long id,
            final MemberCoupon memberCoupon,
            final Long memberId,
            final Money deliveryFee,
            final List<Item> items
    ) {
        this.id = id;
        this.memberCoupon = memberCoupon;
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
        this.items = items;
    }

    public static Order of(
            final Long id,
            final MemberCoupon memberCoupon,
            final Long memberId,
            final Money deliveryFee,
            final List<Item> items
    ) {
        if (Objects.isNull(memberCoupon)) {
            return new Order(id, MemberCoupon.empty(memberId), memberId, deliveryFee, items);
        }
        validate(memberCoupon, items);
        return new Order(id, memberCoupon, memberId, deliveryFee, items);
    }

    public static Order of(final MemberCoupon memberCoupon, final Long memberId, final List<Item> items) {
        if (Objects.isNull(memberCoupon)) {
            return new Order(MemberCoupon.empty(memberId), memberId, items);
        }
        validate(memberCoupon, items);
        return new Order(memberCoupon, memberId, items);
    }

    private static void validate(final MemberCoupon memberCoupon, final List<Item> items) {
        final Money totalPrice = items.stream()
                .map(Item::calculateTotalPrice)
                .reduce(Money.ZERO, Money::plus);
        if (memberCoupon.isInvalidCoupon(totalPrice)) {
            throw new InvalidOrderException("쿠폰을 적용할 수 없는 주문입니다.");
        }
    }

    public Money calculateDiscountPrice() {
        final Money totalPrice = calculateTotalPrice();
        final Money subtrahend = memberCoupon.calculatePrice(totalPrice);
        final Money discountPrice = totalPrice.minus(subtrahend);
        if (discountPrice.isGreaterThanOrEqual(totalPrice)) {
            return totalPrice;
        }
        return discountPrice;
    }

    public Money calculateTotalPrice() {
        return items.stream()
                .map(Item::calculateTotalPrice)
                .reduce(Money.ZERO, Money::plus);
    }

    public Money calculateDeliveryFee() {
        return memberCoupon.calculateDeliveryFee(deliveryFee);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public MemberCoupon getMemberCoupon() {
        return memberCoupon;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public List<Item> getItems() {
        return items;
    }
}
