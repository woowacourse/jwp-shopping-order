package cart.domain;

import java.util.List;

public class Orders {

    private static final Long DEFAULT_DELIVERY_FEE = 3000L;
    private final Long id;
    private final Long deliveryFee;
    private final Coupon coupon;
    private final Member member;

    private final List<OrderItem> orderItems;

    public Orders(final Coupon coupon, final Member member, final List<OrderItem> orderItems) {
        this(null, DEFAULT_DELIVERY_FEE, coupon, member, orderItems);
    }

    public Orders(final Long id, final Long deliveryFee, final Coupon coupon, final Member member,
                  final List<OrderItem> orderItems) {
        this.id = id;
        this.deliveryFee = deliveryFee;
        this.coupon = coupon;
        this.member = member;
        this.orderItems = orderItems;
    }

    public long getTotalItemPrice() {
        return orderItems.stream()
                .mapToLong(it -> {
                    long price = it.getPrice();
                    Integer quantity = it.getQuantity();
                    return price * quantity;
                }).sum();
    }

    public Long getId() {
        return id;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
