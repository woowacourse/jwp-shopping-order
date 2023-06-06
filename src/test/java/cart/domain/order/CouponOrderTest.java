package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cartitem.CartItem;
import cart.domain.coupon.Coupon;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.product.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CouponOrderTest {

    @Test
    @DisplayName("총 주문 금액을 계산하여 반환한다.")
    void getTotalPrice() {
        // given
        final Member 져니 = Member.create(1L, "journey", EncryptedPassword.create("password"));
        final Coupon 신규_가입_축하_쿠폰 = Coupon.create(1L, "신규 가입 축하 쿠폰", 20, 10, LocalDateTime.now().plusDays(10));
        final CartItem 치킨_장바구니_아이템 = new CartItem(1L, 10, new Product(1L,
            "치킨", 20000, "chicken_image_url", false));
        final CartItem 피자_장바구니_아이템 = new CartItem(2L, 5, new Product(2L,
            "피자", 30000, "pizza_image_url", false));
        final CouponOrder 주문 = new CouponOrder(져니, 신규_가입_축하_쿠폰, 3000, LocalDateTime.now(),
            List.of(치킨_장바구니_아이템, 피자_장바구니_아이템), true);

        // when
        final BigDecimal 총_금액 = 주문.getTotalPrice();

        // then
        assertThat(총_금액)
            .isEqualTo(BigDecimalConverter.convert(350_000));
    }

    @Test
    @DisplayName("할인된 총 주문 금액을 계산하여 반환한다.")
    void getDiscountedTotalPrice() {
        // given
        final Member 져니 = Member.create(1L, "journey", EncryptedPassword.create("password"));
        final Coupon 신규_가입_축하_쿠폰 = Coupon.create(1L, "신규 가입 축하 쿠폰", 20, 10, LocalDateTime.now().plusDays(10));
        final CartItem 치킨_장바구니_아이템 = new CartItem(1L, 10, new Product(1L,
            "치킨", 20000, "chicken_image_url", false));
        final CartItem 피자_장바구니_아이템 = new CartItem(2L, 5, new Product(2L,
            "피자", 30000, "pizza_image_url", false));
        final CouponOrder 주문 = new CouponOrder(져니, 신규_가입_축하_쿠폰, 3000, LocalDateTime.now(),
            List.of(치킨_장바구니_아이템, 피자_장바구니_아이템), true);

        // when
        final BigDecimal 할인된_금액 = 주문.getDiscountedTotalPrice();

        // then
        assertThat(할인된_금액)
            .isEqualByComparingTo(new BigDecimal(280000));
    }
}
