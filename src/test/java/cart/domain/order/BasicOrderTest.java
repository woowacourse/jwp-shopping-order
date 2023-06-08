package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cartitem.CartItem;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.product.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BasicOrderTest {

    @Test
    @DisplayName("총 주문 금액을 계산하여 반환한다.")
    void getTotalPrice() {
        // given
        final Member 져니 = Member.create(1L, "journey", EncryptedPassword.create("password"));
        final CartItem 치킨_장바구니_아이템 = new CartItem(1L, 10, new Product(1L, "치킨", 20000, "chicken_image_url", false));
        final CartItem 피자_장바구니_아이템 = new CartItem(2L, 5, new Product(2L, "피자", 30000, "pizza_image_url", false));
        final BasicOrder 주문 = new BasicOrder(져니, 3000, LocalDateTime.now(),
            List.of(치킨_장바구니_아이템, 피자_장바구니_아이템), true);

        // when
        final BigDecimal 총_금액 = 주문.getTotalPrice();

        // then
        assertThat(총_금액)
            .isEqualTo(BigDecimal.valueOf(350_000));
    }

    @Test
    @DisplayName("할인된 총 주문 금액을 계산하여 반환한다.")
    void getDiscountedTotalPrice() {
        // given
        final Member 져니 = Member.create(1L, "journey", EncryptedPassword.create("password"));
        final CartItem 치킨_장바구니_아이템 = new CartItem(1L, 10, new Product(1L,
            "치킨", 20000, "chicken_image_url", false));
        final CartItem 피자_장바구니_아이템 = new CartItem(2L, 5, new Product(2L,
            "피자", 30000, "pizza_image_url", false));
        final BasicOrder 주문 = new BasicOrder(져니, 3000, LocalDateTime.now(),
            List.of(치킨_장바구니_아이템, 피자_장바구니_아이템), true);

        // when
        final BigDecimal 할인된_금액 = 주문.getDiscountedTotalPrice();

        // then
        assertThat(할인된_금액)
            .isEqualTo(BigDecimal.valueOf(350_000));
    }
}
