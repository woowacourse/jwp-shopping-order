package cart.fixture;

import java.util.List;

import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.DiscountPolicy;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixture {

    public static final Member MEMBER_A = new Member(1L, "a@a.com", "1234");
    public static final Member MEMBER_B = new Member(2L, "b@b.com", "1234");

    public static String AUTHORIZATION_HEADER_MEMBER_A = "Basic YUBhLmNvbToxMjM0";
    public static String AUTHORIZATION_HEADER_MEMBER_B = "Basic YkBiLmNvbToxMjM0";
    public static String AUTHORIZATION_HEADER_INVALID = "Basic Y0BjLmNvbToxMjM0";

    public static Product 치킨 = new Product(1L, "치킨", 10000,
            "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static Product 샐러드 = new Product(2L, "샐러드", 20000,
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
    public static Product 피자 = new Product(3L, "피자", 13000,
            "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");
    public static Product 햄버거 = new Product(4L, "햄버거", 8000,
            "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8JUVEJTk2JTg0JUVCJUIyJTg0JUVBJUIxJUIwfGVufDB8fDB8fHww&auto=format&fit=crop&w=800&q=60");
    public static Product 제로콜라 = new Product(5L, "제로 콜라", 2000,
            "https://images.unsplash.com/photo-1630404365865-97ff92feba6a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NTl8fCVFQyVCRCU5QyVFQiU5RCVCQ3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=800&q=60");
    public static Product 감자튀김 = new Product(6L, "감자튀김", 4000,
            "https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8JUVBJUIwJTkwJUVDJTlFJTkwJUVEJThBJTgwJUVBJUI5JTgwfGVufDB8fDB8fHww&auto=format&fit=crop&w=800&q=60");

    public static CartItem CART_ITEM_치킨_MEMBER_A() {
        return new CartItem(1L, MEMBER_A, 치킨, 1);
    }

    public static CartItem CART_ITEM_샐러드_MEMBER_A() {
        return new CartItem(2L, MEMBER_A, 샐러드, 10);
    }

    public static List<CartItem> CART_ITEMS_MEMBER_A = List.of(CART_ITEM_치킨_MEMBER_A(), CART_ITEM_샐러드_MEMBER_A());

    public static OrderItem ORDERED_치킨 = new OrderItem(1L, 치킨, 1);
    public static OrderItem ORDERED_샐러드 = new OrderItem(2L, 샐러드, 10);
    public static OrderItem ORDERED_피자 = new OrderItem(3L, 피자, 2);
    public static OrderItem ORDERED_햄버거 = new OrderItem(4L, 햄버거, 3);
    public static OrderItem ORDERED_제로콜라 = new OrderItem(5L, 제로콜라, 7);
    public static OrderItem ORDERED_감자튀김 = new OrderItem(6L, 감자튀김, 6);

    public static List<OrderItem> ORDER_ITEMS_ONE = List.of(ORDERED_치킨, ORDERED_샐러드, ORDERED_피자);
    public static List<OrderItem> ORDER_ITEMS_TWO = List.of(ORDERED_햄버거, ORDERED_제로콜라, ORDERED_감자튀김);
    public static Order ORDER_ONE_MEMBER_A = new Order(1L, MEMBER_A, ORDER_ITEMS_ONE);
    public static Order ORDER_TWO_MEMBER_A = new Order(2L, MEMBER_A, ORDER_ITEMS_TWO);

    public static OrderItem ORDERED_치킨_COUPON_USED = new OrderItem(null, 치킨, 1);

    public static Coupon COUPON_FIXED_2000 = new Coupon(1L, 2000, DiscountPolicy.FIXED);
    public static Coupon COUPON_PERCENTAGE_50 = new Coupon(2L, 50, DiscountPolicy.PERCENTAGE);

    public static MemberCoupon MEMBER_A_COUPON_FIXED_2000() {
        return new MemberCoupon(1L, MEMBER_A, COUPON_FIXED_2000);
    }

    public static MemberCoupon MEMBER_A_COUPON_PERCENTAGE_50() {
        return new MemberCoupon(2L, MEMBER_A, COUPON_PERCENTAGE_50);
    }
}
