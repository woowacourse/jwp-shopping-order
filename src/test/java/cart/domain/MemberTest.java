package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MemberTest {

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "jeomxon@gmail.com", "abcd1234@");
    }

    @DisplayName("동일한 비밀번호를 받으면 true를 반환한다.")
    @Test
    void isSamePasswordTrue() {
        // given
        String newPassword = "abcd1234@";

        // when
        boolean isSame = member.isSamePassword(newPassword);

        // then
        assertThat(isSame).isTrue();
    }

    @DisplayName("다른 비밀번호를 받으면 false를 반환한다.")
    @Test
    void isSamePasswordFalse() {
        // given
        String newPassword = "dcba1234@";

        // when
        boolean isSame = member.isSamePassword(newPassword);

        // then
        assertThat(isSame).isFalse();
    }

    @DisplayName("추가된 금액을 받으면 현재 가진 금액에 더한다.")
    @Test
    void addTotalPurchaseAmount() {
        // given
        int additionalPrice = 10_000;

        // when
        member.addTotalPurchaseAmount(additionalPrice);

        // then
        assertThat(member.getTotalPurchaseAmount()).isEqualTo(10_000);
    }

    @DisplayName("총 금액에 따라 다른 등급을 가진다.")
    @ParameterizedTest
    @CsvSource(value = {"100_000:SILVER", "200_000:GOLD", "300_000:PLATINUM", "500_000:DIAMOND"}, delimiter = ':')
    void upgradeGrade(int givenValue, String grade) {
        // given
        member.addTotalPurchaseAmount(givenValue);

        // when
        member.upgradeGrade();

        // then
        assertThat(member.getGrade().name()).isEqualTo(grade);
    }
}
