package cart.domain.fixture;

import cart.domain.CartItem;
import cart.domain.Money;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.discountPolicy.PricePolicy;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.order.OrderItem;

public class Fixture {

    public static Member member1 = new Member(1L, "pizza1@pizza.com", "pizza");
    public static Member member2 = new Member(2L, "pizza2@pizza.com", "pizza");

    public static Coupon coupon1 = new Coupon(1L, "30000원 이상 3000원 할인 쿠폰", new PricePolicy(), 3000L, new Money(30000L));
    public static Coupon coupon2 = new Coupon(2L, "50000원 이상 5000원 할인 쿠폰", new PricePolicy(), 5000L, new Money(50000L));

    public static MemberCoupon memberCoupon1 = new MemberCoupon(1L, member1, coupon1, false);
    public static MemberCoupon memberCoupon2 = new MemberCoupon(2L, member2, coupon2, false);

    public static Product product1 = new Product(1L, "치즈피자", "치즈피자.png", new Money(8900L));
    public static Product product2 = new Product(2L, "불고기피자", "불고기피자.png", new Money(7900L));
    public static CartItem cartItem1 = new CartItem(1L, 2, member1.getId(), product1);
    public static CartItem cartItem2 = new CartItem(2L, 3, member2.getId(), product2);
    public static OrderItem orderItem1 = new OrderItem(1L, "치즈피자", "치즈피자.png", new Money(8900L), 2);
    public static OrderItem orderItem2 = new OrderItem(2L, "불고기피자", "불고기피자.png", new Money(7900L), 3);
}
