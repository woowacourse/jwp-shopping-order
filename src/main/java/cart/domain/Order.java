package cart.domain;

import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.exception.OrderException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Member member;
    private final Coupon coupon;
    private final Money deliveryFee;
    private final OrderStatus status;
    private final List<OrderItem> orderItems;

    private final Timestamp createdAt;

    private Order(final Long id,
                  final Member member,
                  final Coupon coupon,
                  final Money deliveryFee,
                  final OrderStatus status,
                  final List<OrderItem> orderItems,
                  final Timestamp createdAt) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
    }

    public Order(final Member member,
                 final Coupon coupon,
                 final Money deliveryFee,
                 final List<OrderItem> orderItems) {
        this(null, member, coupon, deliveryFee, cart.domain.OrderStatus.COMPLETE, orderItems, null);
    }

    public static Order of(final OrderEntity order,
                           final List<OrderItemEntity> orderItems,
                           final Coupon coupon) {
        return new Order(order.getId(),
                new Member(order.getMemberId()),
                coupon,
                new Money(order.getDeliveryFee()),
                OrderStatus.find(order.getStatus()),
                OrderItem.from(orderItems),
                order.getCreatedAt());
    }

    public static Order of(final OrderEntity order,
                           final List<OrderItemEntity> orderItems) {
        return new Order(order.getId(),
                new Member(order.getMemberId()),
                null,
                new Money(order.getDeliveryFee()),
                OrderStatus.find(order.getStatus()),
                OrderItem.from(orderItems),
                order.getCreatedAt());
    }

    public void checkTotalPrice(final Money totalPrice) {
        if (!Objects.equals(totalProductPrice(), totalPrice)) {
            throw new OrderException.OutOfDatedProductPrice();
        }
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

    public Order cancel() {
        if (isCanceled()) {
            throw new OrderException.AlreadyCanceledOrder(id);
        }
        return new Order(id, member, coupon, deliveryFee, OrderStatus.CANCEL, orderItems, createdAt);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
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

    public boolean isCanceled() {
        return status == OrderStatus.CANCEL;
    }
}
