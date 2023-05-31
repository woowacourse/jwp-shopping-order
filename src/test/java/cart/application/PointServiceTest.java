package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.Point;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PointServiceTest {

    @Mock
    MemberDao memberDao;

    @InjectMocks
    PointService pointService;

    @Test
    void 회원의_포인트를_증가시킨다() {
        Point point = new Point(500);
        Member member = new Member(null, "gray@google.com", "1234", 1000);

        pointService.increasePoint(member, point);

        assertThat(member.getPoint().getAmount()).isEqualTo(1500);
    }

    @ParameterizedTest
    @CsvSource({"0,1000", "500,500", "1000,0"})
    void 회원의_포인트를_차감시킨다(long amount, long expect) {
        Point point = new Point(amount);
        Member member = new Member(null, "gray@google.com", "1234", 1000);

        pointService.decreasePoint(member, point);

        assertThat(member.getPoint().getAmount()).isEqualTo(expect);
    }

    @ParameterizedTest
    @ValueSource(longs = {1001, 100_000})
    void 회원의_포인트보다_큰_포인트를_차감시키면_예외가_발생한다(long amount) {
        Point point = new Point(amount);
        Member member = new Member(null, "gray@google.com", "1234", 1000);

        assertThatThrownBy(() -> pointService.decreasePoint(member, point))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
