package fixture;

import static fixture.MemberFixture.유저_1;
import static fixture.MemberFixture.유저_2;
import static fixture.ProductFixture.상품_샐러드;
import static fixture.ProductFixture.상품_치킨;
import static fixture.ProductFixture.상품_피자;

import cart.domain.CartItem;

public class CartItemFixture {

    public static final CartItem 장바구니_유저_1_치킨_2개 = new CartItem(1L, 2, 상품_치킨, 유저_1);
    public static final CartItem 장바구니_유저_1_샐러드_4개 = new CartItem(2L, 4, 상품_샐러드, 유저_1);
    public static final CartItem 장바구니_유저_2_피자_5개 = new CartItem(3L, 5, 상품_피자, 유저_2);

}
