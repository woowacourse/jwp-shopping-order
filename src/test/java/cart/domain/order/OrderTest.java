package cart.domain.order;

import cart.domain.Money;
import cart.domain.coupon.MemberCoupon;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import cart.domain.product.CartItem;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static cart.domain.fixture.CouponFixture.AMOUNT_1000_COUPON;
import static cart.domain.fixture.MemberFixture.MEMBER_A;
import static cart.domain.fixture.ProductFixture.PRODUCT_A;
import static cart.domain.fixture.ProductFixture.PRODUCT_B;
import static cart.domain.fixture.ProductFixture.PRODUCT_C;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
class OrderTest {

    @Autowired
    DiscountPolicyProvider discountPolicyProvider;

    @Test
    @DisplayName("주문을 만든다")
    void make_order_test() {
        // given
        List<CartItem> cartItems = List.of(new CartItem(MEMBER_A, PRODUCT_A), new CartItem(MEMBER_A, PRODUCT_B), new CartItem(MEMBER_A, PRODUCT_C));
        List<MemberCoupon> coupons = List.of(new MemberCoupon(AMOUNT_1000_COUPON));
        Money deliveryFee = new Money(3000);

        // when
        final Order order = Order.make(cartItems, coupons, deliveryFee, MEMBER_A, discountPolicyProvider);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(order.getOriginalTotalItemPrice()).isEqualTo(new Money(13000));
            softly.assertThat(order.getActualTotalItemPrice()).isEqualTo(new Money(12000));
        });
    }

}
