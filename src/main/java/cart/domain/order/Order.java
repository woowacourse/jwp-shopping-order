package cart.domain.order;

import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final List<OrderItem> orderItems;
    private final Member member;
    @Nullable
    private final Coupon coupon;
    private final int price;
    private final LocalDateTime date;

    public Order(final List<OrderItem> orderItems, final Member member, final Coupon coupon, final int price) {
        this(null, orderItems, member, coupon, price, LocalDateTime.now());
    }

    public Order(final Long id, final List<OrderItem> orderItems, final Member member, final Coupon coupon, final int price, final LocalDateTime date) {
        this.id = id;
        this.orderItems = orderItems;
        this.member = member;
        this.coupon = coupon;
        this.price = price;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Member getMember() {
        return member;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public int getPrice() {
        return price;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
