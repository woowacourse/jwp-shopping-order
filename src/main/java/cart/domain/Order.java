package cart.domain;

import cart.exception.OrderException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Coupon coupon;
    private final Money deliveryFee;
    private final OrderStatus status;
    private final List<OrderItem> orderItems;

    private final Timestamp createdAt;

    public Order(final Long id,
                 final Coupon coupon,
                 final Money deliveryFee,
                 final OrderStatus status,
                 final List<OrderItem> orderItems,
                 final Timestamp createdAt) {
        this.id = id;
        this.coupon = coupon;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
    }

    public Order(final Coupon coupon,
                 final Money deliveryFee,
                 final List<OrderItem> orderItems) {
        this(null, coupon, deliveryFee, cart.domain.OrderStatus.COMPLETE, orderItems, null);
    }

    public Order complete(final Money totalPrice) {
        checkTotalPrice(totalPrice);
        if (Objects.nonNull(coupon)) {
            return new Order(id, coupon.use(), deliveryFee, OrderStatus.COMPLETE, orderItems, createdAt);
        }
        return new Order(id, null, deliveryFee, OrderStatus.COMPLETE, orderItems, createdAt);
    }

    private void checkTotalPrice(final Money totalPrice) {
        if (!Objects.equals(totalProductPrice(), totalPrice)) {
            throw new OrderException.OutOfDatedProductPrice();
        }
    }

    public Order cancel() {
        if (isCanceled()) {
            throw new OrderException.AlreadyCanceledOrder(id);
        }
        if (Objects.nonNull(coupon)) {
            return new Order(id, coupon.refund(), deliveryFee, OrderStatus.CANCEL, orderItems, createdAt);
        }
        return new Order(id, null, deliveryFee, OrderStatus.CANCEL, orderItems, createdAt);
    }

    private boolean isCanceled() {
        return status == OrderStatus.CANCEL;
    }

    public boolean hasCoupon() {
        return Objects.nonNull(coupon);
    }

    public Money totalProductPrice() {
        final long sum = orderItems.stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
        return new Money(sum);
    }

    public Money totalPayments() {
        final Money totalOrderPrice = totalProductPrice().plus(deliveryFee);
        if (Objects.nonNull(coupon)) {
            return coupon.discount(totalOrderPrice);
        }
        return totalOrderPrice;
    }

    public Long getId() {
        return id;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public Long getCouponId() {
        if (Objects.nonNull(coupon)) {
            return coupon.getId();
        }
        return null;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
