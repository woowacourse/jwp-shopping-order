package cart.domain;

import cart.domain.common.Money;
import cart.domain.coupon.Coupon;
import java.util.List;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Coupon coupon;
    private final Long memberId;
    private final Money deliveryFee;
    private final List<Item> items;

    public Order(final Coupon coupon, final Long memberId, final List<Item> items) {
        this(null, coupon, memberId, Money.from(3000L), items);
    }

    public Order(
            final Long id,
            final Coupon coupon,
            final Long memberId,
            final Money deliveryFee,
            final List<Item> items
    ) {
        this.id = id;
        this.coupon = coupon;
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
        this.items = items;
    }

    public Money calculateDiscountPrice() {
        final Money totalPrice = calculateTotalPrice();
        final Money subtrahend = coupon.calculatePrice(totalPrice);
        return totalPrice.minus(subtrahend);
    }

    public Money calculateTotalPrice() {
        return items.stream()
                .map(Item::calculateTotalPrice)
                .reduce(Money.ZERO, Money::plus);
    }

    public Money calculateDeliveryFee() {
        return coupon.calculateDeliveryFee(calculateTotalPrice(), deliveryFee);
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

    public List<Item> getItems() {
        return items;
    }
}
