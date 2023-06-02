package cart.domain.order;

import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final OrderItems orderItems;
    private final Member member;
    @Nullable
    private final Coupon coupon;
    private final Price price;
    private final LocalDateTime date;

    public Order(final List<OrderItem> orderItems, final Member member, final Coupon coupon, final int price) {
        this(null, orderItems, member, coupon, price, LocalDateTime.now());
    }

    public Order(final Long id, final List<OrderItem> orderItems, final Member member, final Coupon coupon, final int price, final LocalDateTime date) {
        this.id = id;
        this.orderItems = OrderItems.from(orderItems);
        this.member = member;
        this.coupon = coupon;
        this.price = Price.from(price);
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems.getOrderItems();
    }

    public Member getMember() {
        return member;
    }

    @Nullable
    public Coupon getCoupon() {
        return coupon;
    }

    public Price getPrice() {
        return price;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
