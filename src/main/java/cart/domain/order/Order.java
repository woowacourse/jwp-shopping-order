package cart.domain.order;

import cart.domain.Item;
import cart.domain.Items;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.exception.AuthorizationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cart.exception.ErrorCode.NOT_AUTHORIZATION_MEMBER;

public class Order {

    private final Long id;
    private final Member member;
    private final Items items;
    private final Optional<Coupon> coupon;
    private final int totalPrice;
    private final int deliveryPrice;
    private final LocalDateTime orderedAt;

    public Order(final Member member, List<Item> items, final Coupon coupon) {
        this.id = null;
        this.member = member;
        this.items = new Items(items);
        this.coupon = Optional.ofNullable(coupon);
        this.totalPrice = this.items.calculateItemsQuantity(items);
        this.deliveryPrice = 3000;
        this.orderedAt = LocalDateTime.now();
    }

    public Order(final Long id, final Member member, final List<Item> items, final Coupon coupon, final int totalPrice, final int deliveryPrice, final LocalDateTime orderedAt) {
        this.id = id;
        this.member = member;
        this.items = new Items(items);
        this.coupon = Optional.ofNullable(coupon);
        this.totalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
        this.orderedAt = orderedAt;
    }

    public Order(final Long id, final Member member, final List<Item> items, final int totalPrice, final int deliveryPrice, final LocalDateTime orderedAt) {
        this.id = id;
        this.member = member;
        this.items = new Items(items);
        this.coupon = Optional.empty();
        this.totalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
        this.orderedAt = orderedAt;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new AuthorizationException(NOT_AUTHORIZATION_MEMBER);
        }
    }

    public int getDiscountedTotalPrice() {
        return totalPrice - getCouponDiscountPrice();
    }

    public int getCouponDiscountPrice() {
        return coupon.map(value -> (int) (totalPrice * value.getDiscountRate() * 0.01))
                .orElse(0);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Items getItems() {
        return items;
    }

    public Optional<Coupon> getCoupon() {
        return coupon;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
