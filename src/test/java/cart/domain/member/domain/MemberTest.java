package cart.domain.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("기본 멤버 생성 시 금액을 5000원 가진다.")
    void generate_member_default_5000_cash() {
        // when
        Member member = new Member(1L, "aa@naver.com", "password1");

        // then
        assertThat(member.getCash()).isEqualTo(5000);
    }

    @Test
    @DisplayName("금액 충전 시 충전된 금액을 가진 멤버가 반환된다.")
    void chargeCash() {
        // given
        Member member = new Member(1L, "aa@naver.com", "password1");

        // when
        Member chargedMember = member.chargeCash(10000);

        // then
        assertThat(chargedMember.getCash()).isEqualTo(15000);
    }
}
