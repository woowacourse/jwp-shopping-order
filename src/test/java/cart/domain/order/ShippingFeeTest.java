package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayName("배송비 도메인 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ShippingFeeTest {

    @Test
    void 총주문금액이_30000원_이상이면_무료배송이다() {
        // given
        int totalOrderAmount = 30_000;

        // when
        ShippingFee shippingFee = ShippingFee.fromTotalOrderAmount(totalOrderAmount);

        // then
        assertThat(shippingFee.getValue()).isEqualTo(0);
    }

    @Test
    void 총주문금액이_30000원보다_적으면_배송비는_3000원이다() {
        // given
        int totalOrderAmount = 29_000;

        // when
        ShippingFee shippingFee = ShippingFee.fromTotalOrderAmount(totalOrderAmount);

        // then
        assertThat(shippingFee.getValue()).isEqualTo(3000);
    }
}
