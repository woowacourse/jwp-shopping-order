package cart.domain.refund;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cartitem.CartItem;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.order.BasicOrder;
import cart.domain.product.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FullRefundPolicyTest {

    private FullRefundPolicy fullRefundPolicy;

    @BeforeEach
    void setUp() {
        fullRefundPolicy = new FullRefundPolicy();
    }

    @ParameterizedTest(name = "주문한 지 3일 이내인지 확인한다.")
    @CsvSource(value = {"0:true", "2:true", "3:false", "4:false"}, delimiter = ':')
    void isAvailable(final int day, final boolean expected) {
        // given
        final Member 져니 = Member.create(1L, "journey", EncryptedPassword.create("password"));
        final CartItem 치킨_장바구니_아이템 = new CartItem(1L, 10, new Product(1L, "치킨", 20000, "chicken_image_url", false));
        final CartItem 피자_장바구니_아이템 = new CartItem(2L, 5, new Product(2L, "피자", 30000, "pizza_image_url", false));
        final BasicOrder 주문 = new BasicOrder(져니, 3000, LocalDateTime.now().minusDays(day),
            List.of(치킨_장바구니_아이템, 피자_장바구니_아이템), true);
        final LocalDateTime currentTime = LocalDateTime.now();

        // when
        final boolean result = fullRefundPolicy.isAvailable(주문, currentTime);

        // then
        Assertions.assertThat(result)
            .isSameAs(expected);
    }

    @Test
    @DisplayName("할인 금액을 전체 금액 그대로 반환한다.")
    void calculatePrice() {
        // when
        final BigDecimal result = fullRefundPolicy.calculatePrice(BigDecimal.valueOf(10000));

        // then
        assertThat(result)
            .isEqualTo(BigDecimal.valueOf(10000));
    }
}
