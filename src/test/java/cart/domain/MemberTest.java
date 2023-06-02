package cart.domain;

import cart.domain.member.Member;
import cart.domain.vo.Money;
import cart.exception.MemberException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static cart.fixture.domain.MoneyFixture.금액;
import static cart.fixture.domain.MoneyFixture.포인트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberTest {

    private String 비밀번호 = "1234a";

    @Nested
    class 이메일은 {

        @ParameterizedTest
        @ValueSource(strings = {"", "a"})
        void 올바른_형식이_아니면_예외를_던진다(String 잘못된_이메일) {
            assertThatThrownBy(() -> new Member(잘못된_이메일, 비밀번호, 금액("1000"), 포인트("1000")))
                    .isInstanceOf(MemberException.InvalidEmail.class)
                    .hasMessageContaining("이메일 형식을 확인해주세요.");
        }

        @Test
        void 입력하지_않으면_예외를_던진다() {
            assertThatThrownBy(() -> new Member(null, 비밀번호, 금액("1000"), 포인트("1000")))
                    .isInstanceOf(MemberException.InvalidEmail.class)
                    .hasMessageContaining("이메일 형식을 확인해주세요.");
        }
    }

    @Test
    void 회원이_결제할_때_금액이_부족할_경우_예외가_발생한다() {
        // given
        Member member = new Member("a@a.com", "1234", Money.ZERO, Money.ZERO);

        // expect
        assertThatThrownBy(() -> member.spendMoney(Money.from(1000)))
                .isInstanceOf(MemberException.NotEnoughMoney.class);
    }

    @Test
    void 회원이_결제할_때_포인트가_부족할_경우_예외가_발생한다() {
        // given
        Member member = new Member("a@a.com", "1234", Money.ZERO, Money.ZERO);

        // expect
        assertThatThrownBy(() -> member.spendPoint(Money.from(1000)))
                .isInstanceOf(MemberException.NotEnoughPoint.class);
    }

    @Test
    void 같은_비밀번호_인지_확인한다() {
        // given
        Member member = new Member("a@a.com", "1234", Money.ZERO, Money.ZERO);

        // when
        boolean isSamePassword = member.isSamePassword("1234");

        // then
        assertThat(isSamePassword).isTrue();
    }

    @Test
    void 포인트를_적립한다() {
        // given
        Member member = new Member("a@a.com", "1234", Money.ZERO, Money.ZERO);

        // when
        member.accumulatePoint(Money.from(1000));

        // then
        assertThat(member.getPoint()).isEqualTo(Money.from(1000));
    }

    @Test
    void 동일한_회원인지_확인한다() {
        // given
        Member member = new Member("a@a.com", "1234", Money.ZERO, Money.from(1000));

        // when
        boolean isNotSameMember = member.isNotSame(member);

        // then
        assertThat(isNotSameMember).isFalse();
    }
}
