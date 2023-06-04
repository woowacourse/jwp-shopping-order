package cart.fixture;

import cart.domain.cartItem.CartItem;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.order.OrderItem;
import cart.domain.product.Product;
import cart.entity.OrderItemEntity;

import java.util.List;

public class Fixture {

    public static final Product 치킨 = new Product(
            1L,
            "치킨",
            10000,
            "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"
    );

    public static final Product 피자 = new Product(
            3L,
            "피자",
            13000,
            "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"
    );

    public static final Coupon 쿠폰 = new Coupon(1L, "오픈 기념 쿠폰", new Discount("rate", 10));
    public static final MemberCoupon 멤버_쿠폰 = new MemberCoupon(1L, new Coupon(1L, "오픈 기념 쿠폰", new Discount("rate", 10)), false);
    public static final MemberCoupon 멤버_쿠폰2 = new MemberCoupon(2L, new Coupon(2L, "오픈 기념 쿠폰", new Discount("price", 2000)), false);
    public static final Member 유저 = new Member(1L, "a@a.com", "1234");
    public static final Member 유저2 = new Member(2L, "b@b.com", "1234");
    public static final OrderItem 주문_제품_피자 = new OrderItem(new OrderItemEntity(1L, 2L, 피자, 5), List.of());
    public static final OrderItem 주문_제품_치킨 = new OrderItem(new OrderItemEntity(2L, 2L, 치킨, 5), List.of(Fixture.멤버_쿠폰));
    public static final CartItem 장바구니_치킨 = new CartItem(유저, 치킨);
    public static final CartItem 장바구니_피자 = new CartItem(유저, 피자);
}
