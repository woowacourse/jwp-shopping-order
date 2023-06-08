package cart.step2.order.domain;

import cart.step2.order.exception.PriceIsNotValid;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final Integer price;
    private final Long couponId;
    private final Long memberId;
    private final LocalDateTime createdAt;
    private final List<OrderItem> orderItems;

    private Order(final Long id, final Integer price, final Long couponId, final Long memberId, final LocalDateTime createdAt, final List<OrderItem> orderItems) {
        this.id = id;
        this.price = price;
        this.couponId = couponId;
        this.memberId = memberId;
        this.createdAt = createdAt;
        this.orderItems = orderItems;
    }

    public static Order createNonePkAndOrderItemsOrder(final Integer price, final Long couponId, final Long memberId) {
        return new Order(null, price, couponId, memberId, null, null);
    }

    public static Order of(final Long id, final Integer price, final Long couponId, final Long memberId, final LocalDateTime date, final List<OrderItem> orderItems) {
        return new Order(id, price, couponId, memberId, date, orderItems);
    }

    public void validatePrice(final int discountAmount) {
        if (price != getTotalPriceAdaptCoupon(discountAmount)) {
            throw PriceIsNotValid.THROW;
        }
    }

    private int getTotalPriceAdaptCoupon(final int couponPrice) {
        int totalPriceNonAdaptCoupon = orderItems.stream()
                .mapToInt(orderItem -> (orderItem.getProduct().getPrice()) * orderItem.getQuantity())
                .sum();
        return totalPriceNonAdaptCoupon - couponPrice;
    }

    public Long getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

}
