package cart.fixture;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.math.BigDecimal;

public class TestFixture {

    public static final Member 밀리 = new Member(1L, "millie@email.com", "millie");
    public static final Member 박스터 = new Member(2L, "boxster@email.com", "boxster");

    public static final Product 치킨_10000원 = new Product(1L, "치킨", BigDecimal.valueOf(10000), "http://chicken.com");
    public static final Product 피자_20000원 = new Product(2L, "피자", BigDecimal.valueOf(20000), "http://pizza.com");
    public static final Product 햄버거_7000원 = new Product(3L, "햄버거", BigDecimal.valueOf(7000), "http://hamburger.com");
    public static final Product 샌드위치_5000원 = new Product(4L, "샌드위치", BigDecimal.valueOf(5000), "http://sandwich.com");

    public static final CartItem 장바구니_밀리_치킨_10개 = new CartItem(1L, 10, 치킨_10000원, 밀리);
    public static final CartItem 장바구니_밀리_피자_1개 = new CartItem(2L, 1, 피자_20000원, 밀리);
    public static final CartItem 장바구니_밀리_햄버거_4개 = new CartItem(3L, 4, 햄버거_7000원, 밀리);

    public static final CartItem 장바구니_박스터_치킨_200개 = new CartItem(4L, 200, 치킨_10000원, 박스터);
    public static final CartItem 장바구니_박스터_샌드위치_300개 = new CartItem(5L, 300, 샌드위치_5000원, 박스터);
    public static final CartItem 장바구니_박스터_피자_1000개 = new CartItem(6L, 1000, 피자_20000원, 박스터);
}
