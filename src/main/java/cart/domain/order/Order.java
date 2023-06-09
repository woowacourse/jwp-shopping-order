package cart.domain.order;

import cart.domain.cart.CartItems;
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

    public Order(final OrderItems orderItems, final Member member, final Coupon coupon, final Price price) {
        this(null, orderItems, member, coupon, price, LocalDateTime.now());
    }

    public Order(final Long id, final OrderItems orderItems, final Member member, @Nullable final Coupon coupon, final Price price, final LocalDateTime date) {
        this.id = id;
        this.orderItems = orderItems;
        this.member = member;
        this.coupon = coupon;
        this.price = price;
        this.date = date;
    }

    public static Order create(final CartItems cartItems, final Coupon coupon, final Price price) {
        final OrderItems orderItems = cartItems.mapToOrderItems();
        final Member member = cartItems.getMember();

        return new Order(orderItems, member, coupon, price);
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
