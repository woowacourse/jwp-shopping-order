package cart.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import cart.exception.point.HavingPointIsLessThanStandardPointException;
import cart.exception.point.HavingPointIsLessThanUsePointException;
import cart.exception.point.UsePointIsLessThanStandardPointException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("PointPolicy 단위테스트")
class PointPolicyTest {

    @Test
    void 포인트를_사용한다() {
        // given
        Member member = new Member("a@a.com", "1234", new Point(5000));

        // when
        PointPolicy.usePoint(member, new Point(3000));

        // then
        assertThat(member.getPoint().getValue()).isEqualTo(2000);
    }

    @Test
    void 보유한_포인트가_3000보다_적으면_포인트를_사용할_수_없다() {
        // given
        Member member = new Member("a@a.com", "1234", new Point(2000));

        // when
        // then
        assertThatThrownBy(() -> PointPolicy.usePoint(member, new Point(2000)))
                .isInstanceOf(HavingPointIsLessThanStandardPointException.class);
    }

    @Test
    void 사용하고자_하는_포인트가_3000보다_적으면_포인트를_사용할_수_없다() {
        // given
        Member member = new Member("a@a.com", "1234", new Point(3000));

        // when
        // then
        assertThatThrownBy(() -> PointPolicy.usePoint(member, new Point(1000)))
                .isInstanceOf(UsePointIsLessThanStandardPointException.class);
    }

    @Test
    void 보유한_포인트가_사용하고자_하는_포인트보다_적으면_포인트를_사용할_수_없다() {
        // given
        Member member = new Member("a@a.com", "1234", new Point(3000));

        // when
        // then
        assertThatThrownBy(() -> PointPolicy.usePoint(member, new Point(5000)))
                .isInstanceOf(HavingPointIsLessThanUsePointException.class);
    }

    @Test
    void 총상품금액의_5퍼센트_만큼_포인트를_적립한다() {
        // given
        Member member = new Member("a@a.com", "1234", new Point(2000));

        // when
        PointPolicy.earnPoint(member, 100000);

        // then
        assertThat(member.getPoint().getValue()).isEqualTo(7000);
    }

}
