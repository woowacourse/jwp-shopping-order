package cart.domain;

import cart.domain.member.Member;
import cart.domain.member.Password;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTest {

    @ParameterizedTest
    @CsvSource({"1234567a!, true", "a!1234567, false"})
    void 비밀번호가_같은지_확인한다(final String password, final boolean expected) {
        //given
        final Member member = new Member(1L, "huchu@woowahan.com", "1234567a!", 0);

        //when
        final boolean actual = member.hasSamePassword(new Password(password));

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 이메일_값을_반환한다() {
        //given
        final Member member = new Member(1L, "huchu@woowahan.com", "1234567a!", 0);

        //when
        final String emailValue = member.getEmailValue();

        //then
        assertThat(emailValue).isEqualTo("huchu@woowahan.com");
    }

    @Test
    void 비밀번호_값을_반환한다() {
        //given
        final Member member = new Member(1L, "huchu@woowahan.com", "1234567a!", 0);

        //when
        final String passwordValue = member.getPasswordValue();

        //then
        assertThat(passwordValue).isEqualTo("1234567a!");
    }

    @ParameterizedTest
    @CsvSource({"999, true", "1000, true", "1001, false"})
    void 포인트를_사용할_수_있는지_확인한다(final int point, final boolean expected) {
        //given
        final Member member = new Member(1L, "huchu@woowahan.com", "1234567a!", 1000);

        //when
        final boolean actual = member.canUsePoint(Point.valueOf(point));

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 포인트를_계산한다() {
        //given
        final Member member = new Member(1L, "huchu@woowahan.com", "1234567a!", 1000);
        final Point savePoint = Point.valueOf(100);
        final Point usePoint = Point.valueOf(500);

        //when
        final Member newMember = member.calculatePoint(usePoint, savePoint);

        //then
        assertThat(newMember.getPoint()).isEqualTo(Point.valueOf(600));
    }
}
