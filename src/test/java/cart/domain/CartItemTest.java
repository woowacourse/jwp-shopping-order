package cart.domain;

import static cart.fixture.TestFixture.CART_ITEM_치킨_MEMBER_A;
import static cart.fixture.TestFixture.MEMBER_A_COUPON_FIXED_2000;
import static cart.fixture.TestFixture.MEMBER_A_COUPON_PERCENTAGE_50;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CartItemTest {

    @Test
    void 쿠폰을_적용한다() {
        CartItem cartItem = CART_ITEM_치킨_MEMBER_A();

        List<MemberCoupon> coupons = List.of(MEMBER_A_COUPON_FIXED_2000(), MEMBER_A_COUPON_PERCENTAGE_50());
        cartItem.apply(coupons);

        assertThat(cartItem.getCoupons()).containsExactlyInAnyOrderElementsOf(coupons);
    }

    @Test
    void 쿠폰_적용_가격을_알_수_있다() {
        CartItem cartItem = CART_ITEM_치킨_MEMBER_A();

        List<MemberCoupon> coupons = List.of(MEMBER_A_COUPON_FIXED_2000(), MEMBER_A_COUPON_PERCENTAGE_50());
        cartItem.apply(coupons);

        assertThat(cartItem.getPrice()).isEqualTo(new Money(4000));
    }
}
