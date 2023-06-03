package cart.domain.order;

import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.domain.point.Point;
import cart.persistence.order.dto.OrderDto;

import java.sql.Timestamp;

public class Order {
    private static final double SAVE_PERCENT = 0.01;

    private final Long id;
    private final Member member;
    private final OrderItems orderItems;
    private final Coupons coupons;
    private final int paymentPrice;
    private final Point point;
    private final Timestamp createAt;

    public Order(
            final Member member,
            final OrderItems orderItems,
            final Coupons coupons,
            final int paymentPrice,
            final Point point
    ) {
        this(null, member, orderItems, coupons, paymentPrice, point, null);
    }

    public Order(final OrderDto orderDto, final Coupons coupons, final OrderItems orderItems) {
        this(
                orderDto.getId(),
                orderDto.getMember(),
                orderItems,
                coupons,
                orderDto.getPaymentPrice(),
                orderDto.getPoint(),
                orderDto.getCreatedAt()
        );
    }

    private Order(
            final Long id,
            final Member member,
            final OrderItems orderItems,
            final Coupons coupons,
            final int paymentPrice,
            final Point point,
            final Timestamp createAt
    ) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.coupons = coupons;
        this.paymentPrice = paymentPrice;
        this.point = point;
        this.createAt = createAt;
    }

    public int calculateTotalPrice() {
        return orderItems.calculateOrderItemsPrice();
    }

    public Point getSavePoint() {
        return new Point((int) (paymentPrice * SAVE_PERCENT));
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public Coupons getCoupons() {
        return coupons;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }

    public Point getPoint() {
        return point;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }
}
