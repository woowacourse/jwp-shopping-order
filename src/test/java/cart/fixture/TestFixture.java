package cart.fixture;

import cart.domain.CartItem;
import cart.domain.Item;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixture {

    public static final Member 밀리 = new Member(1L, "millie@email.com", "millie");
    public static final Member 박스터 = new Member(2L, "boxster@email.com", "boxster");

    public static final Coupon 쿠폰_10퍼센트 = new Coupon(1L, "10퍼센트 할인 쿠폰", CouponType.RATE, BigDecimal.valueOf(10),
            new Money(1000));
    public static final MemberCoupon 밀리_쿠폰_10퍼센트 = new MemberCoupon(1L, 밀리, 쿠폰_10퍼센트, LocalDate.of(3000, 6, 16));
    public static final MemberCoupon 밀리_만료기간_지난_쿠폰_10퍼센트 = new MemberCoupon(3L, 밀리, 쿠폰_10퍼센트,
            LocalDate.of(1000, 6, 16));
    public static final MemberCoupon 박스터_쿠폰_10퍼센트 = new MemberCoupon(2L, 박스터, 쿠폰_10퍼센트, LocalDate.of(3000, 6, 16));
    public static final MemberCoupon 가짜_쿠폰 = new MemberCoupon(밀리, Coupon.NONE);

    public static final Product 치킨_10000원 = new Product(1L, "치킨", BigDecimal.valueOf(10000), "http://chicken.com");
    public static final Product 피자_20000원 = new Product(2L, "피자", BigDecimal.valueOf(20000), "http://pizza.com");
    public static final Product 햄버거_3000원 = new Product(3L, "햄버거", BigDecimal.valueOf(3000), "http://hamburger.com");

    public static final Item 상품_치킨_10개 = new Item(치킨_10000원, 10);
    public static final Item 상품_피자_1개 = new Item(피자_20000원, 1);
    public static final Order 주문_밀리_치킨_피자_3000원 = new Order(1L, 밀리, List.of(상품_치킨_10개, 상품_피자_1개), new Money(3000),
            LocalDateTime.of(2023, 6, 1, 16, 0, 0, 0), "202306011600001", Coupon.NONE);
    public static final Item 상품_햄버거_3개 = new Item(햄버거_3000원, 3);
    public static final Order 주문_밀리_치킨_햄버거_3000원 = new Order(2L, 밀리, List.of(상품_치킨_10개, 상품_햄버거_3개), new Money(3000),
            LocalDateTime.of(2023, 6, 1, 17, 0, 0, 0), "202306011700001", Coupon.NONE);
    public static final CartItem 장바구니_밀리_치킨_10개 = new CartItem(1L, 10, 치킨_10000원, 밀리);
    public static final CartItem 장바구니_밀리_피자_1개 = new CartItem(2L, 1, 피자_20000원, 밀리);
}
