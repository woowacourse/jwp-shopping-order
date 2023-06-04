package cart.domain.order;

import cart.domain.VO.Money;
import cart.domain.coupon.Coupon;
import cart.exception.order.InvalidOrderException;
import java.util.List;
import java.util.Objects;

public class Order {

    private static final Money DEFAULT_DELIVERY_FEE = Money.from(3000L);

    private final Long id;
    private final Coupon coupon;
    private final Long memberId;
    private final Money deliveryFee;
    private final List<OrderItem> items;

    private Order(final Coupon coupon, final Long memberId, final List<OrderItem> items) {
        this(null, coupon, memberId, DEFAULT_DELIVERY_FEE, items);
    }

    private Order(
            final Long id,
            final Coupon coupon,
            final Long memberId,
            final Money deliveryFee,
            final List<OrderItem> items
    ) {
        validate(coupon, items, memberId);
        this.id = id;
        this.coupon = coupon;
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
        this.items = items;
    }

    private void validate(final Coupon coupon, final List<OrderItem> items, final Long memberId) {
        final Money totalPrice = items.stream()
                .map(OrderItem::calculateTotalPrice)
                .reduce(Money.ZERO, Money::plus);
        if (coupon.isInvalidPrice(totalPrice)) {
            throw new InvalidOrderException("쿠폰을 적용할 수 없는 주문입니다.");
        }
    }

    public static Order of(
            final Long id,
            final Coupon coupon,
            final Long memberId,
            final Money deliveryFee,
            final List<OrderItem> items
    ) {
        if (Objects.isNull(coupon)) {
            return new Order(id, Coupon.EMPTY, memberId, deliveryFee, items);
        }
        return new Order(id, coupon, memberId, deliveryFee, items);
    }

    public static Order of(final Coupon coupon, final Long memberId, final List<OrderItem> items) {
        if (Objects.isNull(coupon)) {
            return new Order(Coupon.EMPTY, memberId, items);
        }
        return new Order(coupon, memberId, items);
    }

    public void checkOwner(final Long memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new InvalidOrderException("주문의 소유자가 아닙니다.");
        }
    }

    public Money calculateDiscountPrice() {
        final Money totalPrice = calculateTotalPrice();
        final Money subtrahend = coupon.calculatePrice(totalPrice);
        final Money discountPrice = totalPrice.minus(subtrahend);
        if (discountPrice.isGreaterThanOrEqual(totalPrice)) {
            return totalPrice;
        }
        return discountPrice;
    }

    public Money calculateTotalPrice() {
        return items.stream()
                .map(OrderItem::calculateTotalPrice)
                .reduce(Money.ZERO, Money::plus);
    }

    public Money calculateDeliveryFee() {
        return coupon.calculateDeliveryFee(deliveryFee);
    }

    public void useCoupon() {
        coupon.use();
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

    public Coupon getCoupon() {
        return coupon;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}
