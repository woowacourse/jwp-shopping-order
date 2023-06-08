package cart.domain.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CashTest {

    @Test
    @DisplayName("충전할 금액을 받아서 현재 금액에서 충전된 금액을 반환한다.")
    void charge() {
        // given
        Cash currentCash = new Cash(5000);
        int cashToCharge = 10000;

        // when
        Cash chargedCash = currentCash.charge(cashToCharge);

        // when
        assertThat(chargedCash.getCash()).isEqualTo(15000);
    }
}
