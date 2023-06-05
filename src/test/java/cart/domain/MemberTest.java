package cart.domain;

import static cart.fixture.DomainFixture.MEMBER_A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("checkUsedPoints는 사용할 수 있는 포인트를 전달하면 정상적으로 동작한다.")
    void checkUsedPointsSuccessTest() {
        Money usedPoints = new Money(3_500);

        assertDoesNotThrow(() -> MEMBER_A.checkUsedPoints(usedPoints));
    }

    @Test
    @DisplayName("checkUsedPoints는 사용할 수 없는 포인트를 전달하면 예외가 발생한다.")
    void checkUsedPointsFailTest() {
        Money usedPoints = new Money(4_000);

        assertThatThrownBy(() -> MEMBER_A.checkUsedPoints(usedPoints))
                .isInstanceOf(MemberException.TooManyUsedPoints.class)
                .hasMessage("사용할 포인트가 소지 포인트보다 많습니다.");
    }

    @Test
    @DisplayName("usePoints는 사용한 포인트를 전달하면 회원이 갖고 있는 포인트에서 사용한 포인트를 뺀다.")
    void usePointsTest() {
        Money usedPoints = new Money(1_000);

        int actual = MEMBER_A.usePoints(usedPoints).getPoint();

        int expected = 2_500;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("earnPoints는 적립한 포인트를 전달하면 회원이 갖고 있는 포인트에서 적립한 포인트를 더한다.")
    void earnedPointsTest() {
        Money earnedPoints = new Money(1_000);

        int actual = MEMBER_A.earnPoints(earnedPoints).getPoint();

        int expected = 4_500;
        assertThat(actual).isEqualTo(expected);
    }
}
