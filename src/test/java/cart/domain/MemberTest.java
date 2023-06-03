package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.point.HavingPointIsLessThanUsePointException;
import cart.member.application.Member;
import cart.member.application.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Member 단위테스트")
class MemberTest {

    @Test
    void member는_point를_가진다() {
        // given
        // when
        Member member = new Member("a@a.com", "1234", new Point(3000));

        // then
        assertThat(member.getPoint().getValue()).isEqualTo(3000);
    }

    @Test
    void point를_사용할_수_있다() {
        // given
        Member member = new Member("a@a.com", "1234", new Point(3000));

        // when
        member.usePoint(new Point(2000));

        // then
        assertThat(member.getPoint().getValue()).isEqualTo(1000);
    }

    @Test
    void 보유한_point_보다_큰_point를_사용하면_예외가_발생한다() {
        // given
        Member member = new Member("a@a.com", "1234", new Point(3000));

        // when
        // then
        assertThatThrownBy(() -> member.usePoint(new Point(5000)))
                .isInstanceOf(HavingPointIsLessThanUsePointException.class);
    }

    @Test
    void point를_획득할_수_있다() {
        // given
        Member member = new Member("a@a.com", "1234", new Point(3000));

        // when
        member.earnPoint(new Point(2000));

        // then
        assertThat(member.getPoint().getValue()).isEqualTo(5000);
    }

    @Test
    void 비밀번호가_맞는지_체크할_수_있다() {
        // given
        // when
        Member member = new Member("a@a.com", "1234", new Point(3000));

        // then
        assertThat(member.checkPassword("5678")).isFalse();
    }
}
